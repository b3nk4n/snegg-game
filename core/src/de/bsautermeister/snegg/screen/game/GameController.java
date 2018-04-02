package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Logger;

import de.bsautermeister.snegg.common.GameManager;
import de.bsautermeister.snegg.common.Updateable;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.listeners.CollisionListener;
import de.bsautermeister.snegg.model.BodyPart;
import de.bsautermeister.snegg.model.Coin;
import de.bsautermeister.snegg.model.Direction;
import de.bsautermeister.snegg.model.Fruit;
import de.bsautermeister.snegg.model.Snake;
import de.bsautermeister.snegg.model.SnakeHead;

public class GameController implements Updateable {
    private static final Logger LOG = new Logger(GameController.class.getName(), GameConfig.LOG_LEVEL);

    private Snake snake;
    private float timer;

    private int collectedCoins;
    private Coin coin;
    private Fruit fruit;

    private CollisionListener collisionListener;

    public GameController(CollisionListener collisionListener) {
        this.collisionListener = collisionListener;
        snake = new Snake();
        coin = new Coin();
        fruit = new Fruit();
        reset();
    }

    @Override
    public void update(float delta) {
        GameManager.INSTANCE.updateDisplayScore(delta);

        if (GameManager.INSTANCE.isPlaying()) {
            checkInput();
            checkDebugInput();

            timer += delta;
            if (timer >= GameConfig.MOVE_TIME) {
                timer -= GameConfig.MOVE_TIME;

                snake.update(delta);

                checkSnakeOutOfBounds();
                checkCollision();
            }

            if (coin.isCollected()) {
                spawnCoin();
            }
            coin.update(delta);
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

    private void reset() {
        GameManager.INSTANCE.reset();
        collectedCoins = 0;
        coin.reset();
        timer = 0;
    }

    private void checkSnakeOutOfBounds() {
        SnakeHead snakeHead = snake.getHead();
        if (snakeHead.getX() >= GameConfig.WORLD_WIDTH) {
            snakeHead.setX(0);
        } else if (snakeHead.getX() < 0) {
            snakeHead.setX(GameConfig.WORLD_WIDTH - GameConfig.SNAKE_SIZE);
        }

        if (snakeHead.getY() >= GameConfig.MAX_Y) {
            snakeHead.setY(0);
        } else if (snakeHead.getY() < 0) {
            snakeHead.setY(GameConfig.MAX_Y - GameConfig.SNAKE_SIZE);
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
            collisionListener.lose();
        }
    }

    private void checkHeadCoinCollision(SnakeHead head, Coin coin) {
        Rectangle headBounds = head.getCollisionBounds();
        Rectangle coinBounds = coin.getCollisionBounds();

        boolean overlap = Intersector.overlaps(headBounds, coinBounds);

        if (!coin.isCollected() && overlap) {
            snake.insertBodyPart();
            GameManager.INSTANCE.incrementScore(coin.getScore());
            coin.collect();
            collisionListener.hitCoin();

            collectedCoins++;
            if (collectedCoins % GameConfig.FRUIT_SPAWN_INTERVAL == 0) {
                spawnFruit();
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
            collisionListener.hitFruit();
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
        float x = MathUtils.random((int)(GameConfig.WORLD_WIDTH - GameConfig.COLLECTIBLE_SIZE));
        float y = MathUtils.random((int)(GameConfig.MAX_Y - GameConfig.COLLECTIBLE_SIZE));
        coin.release();
        coin.setXY(x, y);
    }

    private void spawnFruit() {
        float x = MathUtils.random((int)(GameConfig.WORLD_WIDTH - GameConfig.COLLECTIBLE_SIZE));
        float y = MathUtils.random((int)(GameConfig.MAX_Y - GameConfig.COLLECTIBLE_SIZE));
        fruit.release();
        fruit.setXY(x, y);
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
