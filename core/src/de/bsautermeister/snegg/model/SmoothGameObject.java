package de.bsautermeister.snegg.model;

import com.badlogic.gdx.math.Interpolation;

public abstract class SmoothGameObject extends GameObject {
    private float sourceX;
    private float sourceY;
    private float targetX;
    private float targetY;

    private float transitionDuration;
    private float transitionTimestep;
    private float transitionDelay;

    public SmoothGameObject(float transitionDuration, float transitionDelay) {
        this.transitionDuration = transitionDuration;
        this.transitionDelay = transitionDelay;
    }

    @Override
    public void reset() {
        transitionTimestep = transitionDuration;
        sourceX = targetX;
        sourceX = targetX;
    }

    @Override
    public void update(float delta) {
        transitionTimestep += delta;
        float progress = Math.min(1.0f, (transitionTimestep - transitionDelay) / transitionDuration);
        //float progress = 1;
        if (progress > 0) {
            float x = Interpolation.smooth.apply(sourceX, targetX, progress);
            float y = Interpolation.smooth.apply(sourceY, targetY, progress);

            if (targetX == x) {
                sourceX = targetX;
            }
            if (targetY == y) {
                sourceY = targetY;
            }

            super.setXY(x, y);
        }
    }

    @Override
    public void setXY(float x, float y) {
        this.sourceX = this.getX();
        this.sourceY = this.getY();
        this.targetX = x;
        this.targetY = y;
        transitionTimestep = 0;
    }

    @Override
    public void setX(float x) {
        this.sourceX = this.getX();
        this.targetX = x;
        transitionTimestep = 0;
    }

    @Override
    public void setY(float y) {
        this.sourceY = this.getY();
        this.targetY = y;
        transitionTimestep = 0;
    }

    @Override
    public void moveX(float deltaX) {
        this.sourceX = this.getX();
        this.targetX += deltaX;
        transitionTimestep = 0;
    }

    @Override
    public void moveY(float deltaY) {
        this.sourceY = this.getY();
        this.targetY += deltaY;
        transitionTimestep = 0;
    }

    public void gotoXY(float x, float y) {
        float diffX = targetX - sourceX;
        float diffY = targetY - sourceY;

        this.sourceX = x;
        this.sourceY = y;
        this.targetX = x + diffX;
        this.targetY = y + diffY;
        super.setXY(x, y);
    }

    public void gotoX(float x) {
        float diff = targetX - sourceX;
        this.sourceX = x;
        this.targetX = x + diff;
        super.setX(x);
    }

    public void gotoY(float y) {
        float diff = targetY - sourceY;
        this.sourceY = y;
        this.targetY = y + diff;
        super.setY(y);
    }
}
