package de.bsautermeister.snegg.model;

import de.bsautermeister.snegg.config.GameConfig;

public class SnakeHead extends GameObject {
    private float targetX;
    private float targetY;

    public SnakeHead() {
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
        reset();
    }

    @Override
    public void reset() {
        targetX = 0;
        targetY = 0;
        setXY(0, 0);
    }

    @Override
    public void update(float delta) {
        float x = getX();
        float y = getY();

        x += (targetX - x) * GameConfig.HEAD_TRANSITION_PROGRESS;
        y += (targetY - y) * GameConfig.HEAD_TRANSITION_PROGRESS;

        setXY(x, y);
    }

    public void smoothMoveX(float deltaX) {
        this.targetX += deltaX;
    }

    public void smoothMoveY(float deltaY) {
        this.targetY += deltaY;
    }
}
