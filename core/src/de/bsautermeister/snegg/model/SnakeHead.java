package de.bsautermeister.snegg.model;

import de.bsautermeister.snegg.config.GameConfig;

public class SnakeHead extends SmoothGameObject {

    public SnakeHead() {
        super(GameConfig.HEAD_TRANSITION_SPEED);
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
        reset();
    }

    @Override
    public void reset() {
        gotoXY(0, 0);
    }
}
