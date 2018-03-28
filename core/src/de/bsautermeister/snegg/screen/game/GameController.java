package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Logger;

import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.model.Coin;
import de.bsautermeister.snegg.model.Direction;
import de.bsautermeister.snegg.model.SnakeHead;

public class GameController {
    private static final Logger LOG = new Logger(GameController.class.getName());

    private SnakeHead snakeHead;
    private float timer;

    private Coin coin;

    public GameController() {
        snakeHead = new SnakeHead();
    }

    public void update(float delta) {
        checkInput();

        timer += delta;
        if (timer >= GameConfig.MOVE_TIME) {
            timer -= GameConfig.MOVE_TIME;

            snakeHead.update();

            checkSnakeOutOfBounds();
        }

        spawnCoin();
    }

    private void checkSnakeOutOfBounds() {
        if (snakeHead.getX() >= GameConfig.WORLD_WIDTH) {
            snakeHead.setX(0);
        } else if (snakeHead.getX() < 0) {
            snakeHead.setX(GameConfig.WORLD_WIDTH - GameConfig.SNAKE_SIZE);
        }

        if (snakeHead.getY() >= GameConfig.WORLD_HEIGHT) {
            snakeHead.setY(0);
        } else if (snakeHead.getY() < 0) {
            snakeHead.setY(GameConfig.WORLD_HEIGHT - GameConfig.SNAKE_SIZE);
        }
    }

    private void checkInput() {
        boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean downPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (leftPressed) {
            snakeHead.setDirection(Direction.LEFT);
        } else if (rightPressed) {
            snakeHead.setDirection(Direction.RIGHT);
        } else if (upPressed) {
            snakeHead.setDirection(Direction.UP);
        } else if (downPressed) {
            snakeHead.setDirection(Direction.DOWN);
        }
    }

    private void spawnCoin() {
        if (coin.isAvailable()) {
            return;
        }

        float coinX = MathUtils.random((int)(GameConfig.WORLD_WIDTH - GameConfig.COIN_SIZE));
        float coinY = MathUtils.random((int)(GameConfig.WORLD_HEIGHT - GameConfig.COIN_SIZE));
        coin.setAvailable(true);
        coin.setXY(coinX, coinY);
    }

    public SnakeHead getHead() {
        return snakeHead;
    }

    public Coin getCoin() {
        return coin;
    }
}
