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

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        updateCollusionBounds();
    }

    private void updateCollusionBounds() {
        collisionBounds.set(x, y, width, height);
    }

    public Rectangle getCollisionBounds() {
        return collisionBounds;
    }
}
