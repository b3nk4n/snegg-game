package de.bsautermeister.snegg.model;

import de.bsautermeister.snegg.config.GameConfig;

public class SnakeHead extends GameObject {
    public SnakeHead() {
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
        reset();
    }

    @Override
    public void reset() {

    }

    @Override
    public void update(float delta) {

    }
}
