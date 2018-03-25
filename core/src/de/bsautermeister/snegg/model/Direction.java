package de.bsautermeister.snegg.model;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public boolean isUp() {
        return this == UP;
    }

    public boolean isDown() {
        return this == DOWN;
    }

    public boolean isLeft() {
        return this == LEFT;
    }

    public boolean isRight() {
        return this == RIGHT;
    }
}
