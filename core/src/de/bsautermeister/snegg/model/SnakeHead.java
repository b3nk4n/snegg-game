package de.bsautermeister.snegg.model;

import de.bsautermeister.snegg.config.GameConfig;

public class SnakeHead extends SmoothGameObject {

    public SnakeHead() {
        super(GameConfig.MOVE_TIME * 2f/3f, 0f);
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        gotoXY(0, 0);
    }
}
