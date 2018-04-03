package de.bsautermeister.snegg.model;

public abstract class SmoothGameObject extends GameObject {
    private float targetX;
    private float targetY;
    private float transitionSpeed;

    public SmoothGameObject(float transitionSpeed) {
        this.transitionSpeed = transitionSpeed;
    }

    @Override
    public void update(float delta) {
        float x = getX();
        float y = getY();

        x += (targetX - x) * transitionSpeed;
        y += (targetY - y) * transitionSpeed;

        super.setXY(x, y);
    }

    @Override
    public void setXY(float x, float y) {
        this.targetX = x;
        this.targetY = y;
    }

    @Override
    public void setX(float x) {
        this.targetX = x;
    }

    @Override
    public void setY(float y) {
        this.targetY = y;
    }

    @Override
    public void moveX(float deltaX) {
        this.targetX += deltaX;
    }

    @Override
    public void moveY(float deltaY) {
        this.targetY += deltaY;
    }

    public void gotoXY(float x, float y) {
        this.targetX = x;
        this.targetY = y;
        super.setXY(x, y);
    }

    public void gotoX(float x) {
        this.targetX = x;
        super.setX(x);
    }

    public void gotoY(float y) {
        this.targetY = y;
        super.setY(y);
    }
}
