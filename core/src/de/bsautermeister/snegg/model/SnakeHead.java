package de.bsautermeister.snegg.model;

import de.bsautermeister.snegg.config.GameConfig;

public class SnakeHead extends GameObject {
    private Direction direction = Direction.RIGHT;

    public SnakeHead() {
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
    }

    public void update() {
        if (direction.isRight()) {
            moveX(GameConfig.SNAKE_SPEED);
        } else if (direction.isLeft()) {
            moveX(-GameConfig.SNAKE_SPEED);
        } else if (direction.isUp()) {
            moveY(GameConfig.SNAKE_SPEED);
        } else if (direction.isDown()) {
            moveY(-GameConfig.SNAKE_SPEED);
        }
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
