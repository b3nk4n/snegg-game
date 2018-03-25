package de.bsautermeister.snegg.model;

import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject {
    protected float x;
    protected float y;

    protected float width = 1f;
    protected float height = 1f;

    protected Rectangle collisionBounds;

    public GameObject() {
        collisionBounds = new Rectangle(x, y, width, height);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollusionBounds();
    }

    public void moveX(float deltaX) {
        x += deltaX;
        updateCollusionBounds();
    }

    public void moveY(float deltaY) {
        y += deltaY;
        updateCollusionBounds();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        updateCollusionBounds();
    }

    public Rectangle getCollisionBounds() {
        return collisionBounds;
    }

    protected void updateCollusionBounds() {
        collisionBounds.set(x, y, width, height);
    }
}
