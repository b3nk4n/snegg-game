package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.utils.Logger;

import de.bsautermeister.snegg.model.SnakeHead;

public class GameController {
    private static final Logger LOG = new Logger(GameController.class.getName());

    private SnakeHead snakeHead;

    public GameController() {
        snakeHead = new SnakeHead();
    }

    public void update(float delta) {

    }

    public SnakeHead getHead() {
        return snakeHead;
    }
}
