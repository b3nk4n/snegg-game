package de.bsautermeister.snegg.model;

import de.bsautermeister.snegg.config.GameConfig;

public class SnakeHead extends SmoothGameObject {

    public SnakeHead() {
        super(0f, 0f); // will be set at runtime
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        gotoXY(1, 1);
    }
}
