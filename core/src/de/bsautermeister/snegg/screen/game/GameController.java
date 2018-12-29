package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.SneggGame;
import de.bsautermeister.snegg.common.GameScore;
import de.bsautermeister.snegg.common.GameState;
import de.bsautermeister.snegg.common.ScoreProvider;
import de.bsautermeister.snegg.common.Updateable;
import de.bsautermeister.snegg.GameConfig;
import de.bsautermeister.snegg.input.DirectionGestureDetector;
import de.bsautermeister.snegg.input.DirectionGestureListener;
import de.bsautermeister.snegg.listeners.GameListener;
import de.bsautermeister.snegg.model.BodyPart;
import de.bsautermeister.snegg.model.Coin;
import de.bsautermeister.snegg.model.Direction;
import de.bsautermeister.snegg.model.Fruit;
import de.bsautermeister.snegg.model.Snake;
import de.bsautermeister.snegg.model.SnakeHead;
import de.bsautermeister.snegg.text.StatusText;
import de.bsautermeister.snegg.text.StatusTextQueue;
import de.bsautermeister.snegg.screen.menu.OverlayCallback;
import de.bsautermeister.snegg.serializer.BinarySerializable;
import de.bsautermeister.snegg.serializer.BinarySerializer;


public class GameController implements Updateable, BinarySerializable {
    private static final Logger LOG = new Logger(GameController.class.getName(), GameConfig.LOG_LEVEL);

    private GameState stateBeforePause;
    private GameState state = GameState.READY;

    private float gameTime;
    private float currentMoveTime;

    private float snakeMoveTimer;
    private Snake snake;

    private int collectedCoins;
    private Coin coin;
    private Fruit fruit;
    private float fruitSpanDelayTimer;

    private GameListener gameListener;

    private OverlayCallback callback;

    private float gameOverTimer;
    private static final float GAME_OVER_WAIT_TIME = 1f;

    private final GameScore gameScore = new GameScore();

    private InputProcessor inputProcessor;

    private boolean gameIsCanced;

    private boolean hasPublishedHighscoreMessage;
    StatusTextQueue statusTextQueue;

    public GameController(final GameListener gameListener) {
        this.gameListener = gameListener;
        snake = new Snake();
        coin = new Coin();
        fruit = new Fruit();

        statusTextQueue = new StatusTextQueue(GameConfig.TEXT_ANIMATION_DURATION);

        callback = new OverlayCallback() {
            @Override
            public void resume() {
                state = GameState.PLAYING;
            }

            @Override
            public void restart() {
                reset();
            }

            @Override
            public void quit() {
                SneggGame.deleteSavedData();
                gameIsCanced = true;
                gameListener.quit();
                gameListener.finishGame(gameScore.getScore());
            }
        };

        // enable phones BACK button
        Gdx.input.setCatchBackKey(true);

        this.inputProcessor = new DirectionGestureDetector(333.0f, new DirectionGestureListener() {
            @Override
            public void onUp() {
                if (state.isPlaying()) {
                    snake.setDirection(Direction.UP);
                }
            }

            @Override
            public void onRight() {
                if (state.isPlaying()) {
                    snake.setDirection(Direction.RIGHT);
                }
            }

            @Override
            public void onDown() {
                if (state.isPlaying()) {
                    snake.setDirection(Direction.DOWN);
                }
            }

            @Override
            public void onLeft() {
                if (state.isPlaying()) {
                    snake.setDirection(Direction.LEFT);
                }
            }
        });

        Gdx.input.setInputProcessor(this.inputProcessor);

        reset();
    }

    private void reset() {
        state = GameState.PLAYING;
        stateBeforePause = GameState.UNDEFINED;
        gameScore.reset();
        collectedCoins = 0;
        fruitSpanDelayTimer = Float.MAX_VALUE;
        gameTime = 0f;
        currentMoveTime = GameConfig.MOVE_TIME;
        snakeMoveTimer = 0f;
        snake.reset();
        gameOverTimer = 0f;
        hasPublishedHighscoreMessage = false;
        fruit.reset();
        coin.reset();
        spawnCoin();
        LOG.debug("SNAKE: y: " + snake.getHead().getY());
    }

    @Override
    public void update(float delta) {
        gameScore.updateDisplayScore(delta);

        if (state.isPlaying()) {
            gameTime += delta;
            currentMoveTime -= delta * GameConfig.DIFFICULTY_LOWERING_MOVE_TIME_FACTOR;
            currentMoveTime = Math.max(GameConfig.MIN_MOVE_TIME, currentMoveTime);

            checkBackButtonInput();
            checkKeyboardInput();
            checkDebugInput();

            snakeMoveTimer += delta;
            if (snakeMoveTimer >= currentMoveTime) {
                snakeMoveTimer = 0;

                snake.setMaxTotalMoveTime(currentMoveTime);
                snake.movementStep();

                snake.checkSnakeHeadOutOfBounds();
                snake.checkSnakeBodyPartsOutOfBounds();

                checkCollision();
            }

            snake.update(delta);

            coin.update(delta);

            fruitSpanDelayTimer -= delta;
            if (fruitSpanDelayTimer <= 0) {
                fruitSpanDelayTimer = Float.MAX_VALUE;
                spawnFruit();
            }
            fruit.update(delta);
        } else if (state.isGameOverPending()) {
            gameOverTimer += delta;
            if (gameOverTimer >= GAME_OVER_WAIT_TIME) {
                SneggGame.deleteSavedData();
                state = GameState.GAME_OVER;
            }
        }

        if (state.isGameActive()) {
            statusTextQueue.update(delta);
        }
    }

    private void checkCollision() {
        SnakeHead head = snake.getHead();
        checkHeadCoinCollision(head, coin);
        checkHeadFruitCollision(head, fruit);
        for (BodyPart bodyPart : snake.getBodyParts()) {
            checkHeadBodyPartCollision(head, bodyPart);
        }
    }

    private void checkHeadBodyPartCollision(SnakeHead head, BodyPart bodyPart) {
        if (bodyPart.isJustAdded()) {
            bodyPart.setJustAdded(false);
            return;
        }

        Rectangle headBounds = head.getCollisionBounds();
        Rectangle bodyBounds = bodyPart.getCollisionBounds();

        if (Intersector.overlaps(headBounds, bodyBounds)) {
            state = GameState.GAME_OVER_PENDING;
            //gameScore.saveHighscore();
            gameListener.lose();
            gameListener.finishGame(gameScore.getScore());
        }
    }

    private void checkHeadCoinCollision(SnakeHead head, Coin coin) {
        Rectangle headBounds = head.getCollisionBounds();
        Rectangle coinBounds = coin.getCollisionBounds();

        boolean overlap = Intersector.overlaps(headBounds, coinBounds);

        if (!coin.isCollected() && overlap) {
            snake.insertBodyPart();
            snake.makeHappy();
            long newScore = gameScore.incrementScore(coin.getScore());
            spawnCoin();
            gameListener.hitCoin(newScore);
            snakeChanged(newScore);

            collectedCoins++;
            if (collectedCoins % GameConfig.FRUIT_SPAWN_INTERVAL == 0) {
                fruitSpanDelayTimer = MathUtils.random(
                        GameConfig.FRUIT_MIN_SPAWN_DELAY,
                        GameConfig.FRUIT_MAX_SPAWN_DELAY);
            }
        }
    }

    private void checkHeadFruitCollision(SnakeHead head, Fruit fruit) {
        Rectangle headBounds = head.getCollisionBounds();
        Rectangle fruitBounds = fruit.getCollisionBounds();

        boolean overlap = Intersector.overlaps(headBounds, fruitBounds);

        if (!fruit.isCollected() && overlap) {
            snake.makeHappy();
            long newScore = gameScore.incrementScore(fruit.getScore());
            fruit.collect();
            gameListener.hitFruit(gameScore.getScore());
            snakeChanged(newScore);
        }
    }

    private void snakeChanged(long newScore) {
        gameListener.snakeChanged(snake.length(), newScore);

        if (!hasPublishedHighscoreMessage) {
            long onlineHighscore = SneggGame.getGameServiceManager().getOnlineHighscore();
            if (onlineHighscore > 0 && newScore > onlineHighscore) {
                hasPublishedHighscoreMessage = true;
                publishMessage("NEW HIGHSCORE");
            }
        }
    }

    public void publishMessage(String message) {
        statusTextQueue.publish(
                new StatusText(message, GameConfig.HUD_CENTER_X, GameConfig.HUD_CENTER_Y));
    }

    private void checkBackButtonInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            pauseGame();
        }
    }

    private void checkKeyboardInput() {
        boolean leftPressed = Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
        boolean rightPressed = Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);
        boolean upPressed = Gdx.input.isKeyJustPressed(Input.Keys.UP);
        boolean downPressed = Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
        boolean escapePressed = Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);

        if (leftPressed) {
            snake.setDirection(Direction.LEFT);
        } else if (rightPressed) {
            snake.setDirection(Direction.RIGHT);
        } else if (upPressed) {
            snake.setDirection(Direction.UP);
        } else if (downPressed) {
            snake.setDirection(Direction.DOWN);
        }

        if (escapePressed) {
            pauseGame();
        }
    }

    private void pauseGame() {
        stateBeforePause = state;
        state = GameState.PAUSED;
    }

    private void checkDebugInput() {
        if (!GameConfig.DEBUG_MODE) {
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            snake.insertBodyPart();
        }
    }

    private void spawnCoin() {
        Vector2 pos = getRandomFreePosition();
        coin.release();
        coin.setXY(pos.x, pos.y);
    }

    private void spawnFruit() {
        Vector2 pos = getRandomFreePosition();
        fruit.release();
        fruit.setXY(pos.x, pos.y);
        gameListener.spawnFruit();
    }

    private Vector2 getRandomFreePosition() {
        int x;
        int y;
        do {
            x = MathUtils.random((int)GameConfig.MIN_X, (int)(GameConfig.MAX_X - GameConfig.COLLECTIBLE_SIZE));
            y = MathUtils.random((int)GameConfig.MIN_Y, (int)(GameConfig.MAX_Y - GameConfig.COLLECTIBLE_SIZE));
        } while (isPositionBlocked(x, y));

        return new Vector2(x, y);
    }

    /**
     * Checks whether the requested positions are blocked by another game object.
     * We use target-positions for SmoothGameObjects, to ensure that a check is not failing due to
     * animation is still in progress.
     * @param x The x position
     * @param y The y position
     * @return Returns True in case the position is blocked, else False.
     */
    private boolean isPositionBlocked(int x, int y) {
        SnakeHead head = snake.getHead();
        if (head.getTargetX() == x && head.getTargetY() == y) {
            return true;
        }

        for (BodyPart body : snake.getBodyParts()) {
            if (body.getTargetX() == x && body.getTargetY() == y) {
                return true;
            }
        }

        if (coin.getX() == x && coin.getY() == y) {
            return true;
        }

        if (fruit.getX() == x && fruit.getY() == y) {
            return true;
        }

        return false;
    }

    public Snake getSnake() {
        return snake;
    }

    public Coin getCoin() {
        return coin;
    }

    public Fruit getFruit() {
        return fruit;
    }

    public GameState getState() {
        return state;
    }

    public OverlayCallback getCallback() {
        return callback;
    }

    public ScoreProvider getScoreProvider() {
        return gameScore;
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public StatusTextQueue getStatusTextQueue() {
        return statusTextQueue;
    }

    public void save() {
        if (getState().isAnyGameOverState() || gameIsCanced) {
            // don't save the game, when the player is game over, otherwise he would resume the game
            // which would end immediately afterwards
            return;
        }

        final FileHandle handle = SneggGame.getSavedDataHandle();
        if (!BinarySerializer.write(this, handle.write(false))) {
            LOG.error("Could not save game state");
        }
    }

    public boolean load() {
        final FileHandle handle = SneggGame.getSavedDataHandle();

        if (handle.exists()) {
            if (!BinarySerializer.read(this, handle.read())) {
                LOG.error("Could not load game state");
            }

            SneggGame.deleteSavedData();
            return true;
        }
        return false;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        if (state.isPaused()) {
            // don't store the paused state, because otherwise the game resumes in the pause menu
            out.writeUTF(stateBeforePause.name());
        } else {
            out.writeUTF(state.name());
        }
        out.writeFloat(gameTime);
        out.writeFloat(currentMoveTime);
        out.writeFloat(gameOverTimer);
        out.writeInt(collectedCoins);
        coin.write(out);
        fruit.write(out);
        snake.write(out);
        gameScore.write(out);
        statusTextQueue.write(out);
        out.writeBoolean(hasPublishedHighscoreMessage);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        state = Enum.valueOf(GameState.class, in.readUTF());
        gameTime = in.readFloat();
        currentMoveTime = in.readFloat();
        gameOverTimer = in.readFloat();
        collectedCoins = in.readInt();
        coin.read(in);
        fruit.read(in);
        snake.read(in);
        gameScore.read(in);
        statusTextQueue.read(in);
        hasPublishedHighscoreMessage = in.readBoolean();
    }
}
