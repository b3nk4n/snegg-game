package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;

import de.bsautermeister.snegg.common.GameManager;
import de.bsautermeister.snegg.common.GameState;
import de.bsautermeister.snegg.common.LocalHighscoreService;
import de.bsautermeister.snegg.common.ScoreProvider;
import de.bsautermeister.snegg.common.Updateable;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.listeners.GameListener;
import de.bsautermeister.snegg.model.BodyPart;
import de.bsautermeister.snegg.model.Coin;
import de.bsautermeister.snegg.model.Direction;
import de.bsautermeister.snegg.model.Fruit;
import de.bsautermeister.snegg.model.Snake;
import de.bsautermeister.snegg.model.SnakeHead;
import de.bsautermeister.snegg.screen.menu.OverlayCallback;


public class GameController implements Updateable {
    private static final Logger LOG = new Logger(GameController.class.getName(), GameConfig.LOG_LEVEL);

    private float gameTime;
    private static float currentMoveTime;

    private float snakeMoveTimer;
    private Snake snake;

    private int collectedCoins;
    private Coin coin;
    private Fruit fruit;
    private float fruitSpanDelayTimer;

    private GameListener gameListener;

    private OverlayCallback callback;

    private float gameOverTimer;
    private static final float GAME_OVER_WAIT_TIME = 3f;

    private final LocalHighscoreService highscoreService = new LocalHighscoreService();

    public GameController(GameListener gameListener) {
        this.gameListener = gameListener;
        snake = new Snake();
        coin = new Coin();
        fruit = new Fruit();

        callback = new OverlayCallback() {
            @Override
            public void resume() {
                GameManager.INSTANCE.setPlaying();
            }

            @Override
            public void quit() {
                // suicide instead of just quitting to also save the current score
                GameManager.INSTANCE.setGameOver();
                highscoreService.saveHighscore();
                // skip wait time
                gameOverTimer = GAME_OVER_WAIT_TIME;
            }
        };

        // enable phones BACK button
        Gdx.input.setCatchBackKey(true);

        reset();
    }

    private void reset() {
        GameManager.INSTANCE.reset();
        highscoreService.reset();
        collectedCoins = 0;
        spawnCoin();
        fruitSpanDelayTimer = Float.MAX_VALUE;
        gameTime = 0f;
        currentMoveTime = GameConfig.MOVE_TIME;
        snakeMoveTimer = 0f;
        snake.reset();
        gameOverTimer = 0f;
    }

    @Override
    public void update(float delta) {
        highscoreService.updateDisplayScore(delta);

        if (GameManager.INSTANCE.isPlaying()) {
            gameTime += delta;
            currentMoveTime -= delta * GameConfig.DIFFICULTY_LOWERING_MOVE_TIME_FACTOR;
            currentMoveTime = Math.max(GameConfig.MIN_MOVE_TIME, currentMoveTime);

            LOG.info(String.valueOf(currentMoveTime));

            checkTouchInput();
            checkKeyboardInput();
            checkDebugInput();

            snakeMoveTimer += delta;
            if (snakeMoveTimer >= currentMoveTime) {
                snakeMoveTimer = 0;

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
        } else if (GameManager.INSTANCE.isGameOver()) {
            gameOverTimer += delta;
        } else {
            checkForRestart();
        }
    }

    private void checkForRestart() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            reset();
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
            GameManager.INSTANCE.setGameOver();
            highscoreService.saveHighscore();
            gameListener.lose();
        }
    }

    private void checkHeadCoinCollision(SnakeHead head, Coin coin) {
        Rectangle headBounds = head.getCollisionBounds();
        Rectangle coinBounds = coin.getCollisionBounds();

        boolean overlap = Intersector.overlaps(headBounds, coinBounds);

        if (!coin.isCollected() && overlap) {
            snake.insertBodyPart();
            highscoreService.incrementScore(coin.getScore());
            spawnCoin();
            gameListener.hitCoin();

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
            highscoreService.incrementScore(fruit.getScore());
            fruit.collect();
            gameListener.hitFruit();
        }
    }

    private void checkTouchInput() {
        if (Gdx.input.isTouched() && Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();

            Vector3 touchPosition = GameRenderer.projectToWorld(x, y);
            float headX = snake.getHead().getX();
            float headY = snake.getHead().getY();

            switch (snake.getDirection()) {
                case UP:
                case DOWN:
                    if (touchPosition.x < headX) {
                        snake.setDirection(Direction.LEFT);
                    } else if (touchPosition.x > headX) {
                        snake.setDirection(Direction.RIGHT);
                    }
                    break;
                case RIGHT:
                case LEFT:
                    if (touchPosition.y < headY) {
                        snake.setDirection(Direction.DOWN);
                    } else if (touchPosition.y > headY) {
                        snake.setDirection(Direction.UP);
                    }
                    break;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            GameManager.INSTANCE.setPaused();
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
            GameManager.INSTANCE.setPaused();
        }
    }

    private void checkDebugInput() {
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
            x = MathUtils.random((int)(GameConfig.WORLD_WIDTH - GameConfig.COLLECTIBLE_SIZE));
            y = MathUtils.random((int)(GameConfig.MAX_Y - GameConfig.COLLECTIBLE_SIZE));
        } while (isPositionBlocked(x, y));

        return new Vector2(x, y);
    }

    private boolean isPositionBlocked(int x, int y) {
        SnakeHead head = snake.getHead();
        if (head.getX() == x && head.getY() == y)
            return true;

        for (BodyPart body : snake.getBodyParts()) {
            if (body.getX() == x || head.getY() == y)
                return true;
        }

        if (coin.getX() == x && coin.getY() == y)
            return true;

        if (fruit.getX() == x && fruit.getY() == y)
            return true;

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
        return GameManager.INSTANCE.getState();
    }

    public OverlayCallback getCallback() {
        return callback;
    }

    public boolean gameOverWaitTimeReady() {
        return gameOverTimer >= GAME_OVER_WAIT_TIME;
    }

    public static float getCurrentMoveTime() {
        return currentMoveTime;
    }

    public ScoreProvider getScoreProvider() {
        return highscoreService;
    }
}
