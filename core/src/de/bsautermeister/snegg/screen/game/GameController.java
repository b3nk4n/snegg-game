package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

import de.bsautermeister.snegg.common.GameManager;
import de.bsautermeister.snegg.common.Updateable;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.listeners.GameListener;
import de.bsautermeister.snegg.model.BodyPart;
import de.bsautermeister.snegg.model.Coin;
import de.bsautermeister.snegg.model.Direction;
import de.bsautermeister.snegg.model.Fruit;
import de.bsautermeister.snegg.model.Snake;
import de.bsautermeister.snegg.model.SnakeHead;

public class GameController implements Updateable {
    private static final Logger LOG = new Logger(GameController.class.getName(), GameConfig.LOG_LEVEL);
    private static final float EPS = 0.025f;

    private float snakeMoveTimer;
    private Snake snake;

    private int collectedCoins;
    private Coin coin;
    private Fruit fruit;
    private float fruitSpanDelayTimer;

    private GameListener gameListener;

    public GameController(GameListener gameListener) {
        this.gameListener = gameListener;
        snake = new Snake();
        coin = new Coin();
        fruit = new Fruit();
        reset();
    }

    private void reset() {
        GameManager.INSTANCE.reset();
        collectedCoins = 0;
        spawnCoin();
        fruitSpanDelayTimer = Float.MAX_VALUE;
        snakeMoveTimer = 0f;
        snake.reset();
    }

    @Override
    public void update(float delta) {
        GameManager.INSTANCE.updateDisplayScore(delta);

        if (GameManager.INSTANCE.isPlaying()) {
            checkInput();
            checkDebugInput();

            checkSnakeOutOfBounds(); // TODO maybe we do not need an extended map bounds, when we also check this only before/after each animation?

            snakeMoveTimer += delta;
            if (snakeMoveTimer >= GameConfig.MOVE_TIME) {
                snakeMoveTimer -= GameConfig.MOVE_TIME;
                snake.movementStep();
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
        } else {
            checkForRestart();
        }
    }

    private void checkForRestart() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            reset();
        }
    }

    private void checkSnakeOutOfBounds() {
        SnakeHead snakeHead = snake.getHead();
        if (snakeHead.getX() >= GameConfig.WORLD_WIDTH - EPS) {
            snakeHead.gotoX(0);
        } else if (snakeHead.getX() < -GameConfig.SNAKE_SIZE + EPS) {
            snakeHead.gotoX(GameConfig.WORLD_WIDTH - GameConfig.SNAKE_SIZE);
        }

        if (snakeHead.getY() >= GameConfig.MAX_Y - EPS) {
            snakeHead.gotoY(0);
        } else if (snakeHead.getY() < -GameConfig.SNAKE_SIZE + EPS) {
            snakeHead.gotoY(GameConfig.MAX_Y - GameConfig.SNAKE_SIZE);
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
            GameManager.INSTANCE.saveHighscore();
            GameManager.INSTANCE.setGameOver();
            gameListener.lose();
        }
    }

    private void checkHeadCoinCollision(SnakeHead head, Coin coin) {
        Rectangle headBounds = head.getCollisionBounds();
        Rectangle coinBounds = coin.getCollisionBounds();

        boolean overlap = Intersector.overlaps(headBounds, coinBounds);

        if (!coin.isCollected() && overlap) {
            snake.insertBodyPart();
            GameManager.INSTANCE.incrementScore(coin.getScore());
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
            GameManager.INSTANCE.incrementScore(fruit.getScore());
            fruit.collect();
            gameListener.hitFruit();
        }
    }

    private void checkInput() {
        boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean downPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (leftPressed) {
            snake.setDirection(Direction.LEFT);
        } else if (rightPressed) {
            snake.setDirection(Direction.RIGHT);
        } else if (upPressed) {
            snake.setDirection(Direction.UP);
        } else if (downPressed) {
            snake.setDirection(Direction.DOWN);
        }
    }

    private void checkDebugInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS)) {
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
}
