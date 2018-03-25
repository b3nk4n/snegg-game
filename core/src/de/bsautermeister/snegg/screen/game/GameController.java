package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Logger;

import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.model.Direction;
import de.bsautermeister.snegg.model.SnakeHead;

public class GameController {
    private static final Logger LOG = new Logger(GameController.class.getName());

    private SnakeHead snakeHead;
    private float timer;

    public GameController() {
        snakeHead = new SnakeHead();
    }

    public void update(float delta) {
        checkInput();

        timer += delta;
        if (timer >= GameConfig.MOVE_TIME) {
            timer -= GameConfig.MOVE_TIME;

            snakeHead.update();
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

    public SnakeHead getHead() {
        return snakeHead;
    }
}
