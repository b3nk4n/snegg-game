package de.bsautermeister.snegg.model;

import com.badlogic.gdx.math.Rectangle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.common.Resettable;
import de.bsautermeister.snegg.common.Updateable;
import de.bsautermeister.snegg.serializer.BinarySerializable;

public abstract class GameObject implements Resettable, Updateable, BinarySerializable {
    protected float x;
    protected float y;

    protected float width = 1f;
    protected float height = 1f;

    protected Rectangle collisionBounds;

    public GameObject() {
        collisionBounds = new Rectangle(x, y, width, height);
    }

    public abstract void update(float delta);

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollusionBounds();
    }

    public void setX(float x) {
        this.x = x;
        updateCollusionBounds();
    }

    public void setY(float y) {
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

    public float getX() {
        return x;
    }

    public float getCenterX() {
        return x + width / 2f;
    }

    public float getCenterY() {
        return y + height / 2f;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(width);
        out.writeFloat(height);
        out.writeFloat(collisionBounds.x);
        out.writeFloat(collisionBounds.y);
        out.writeFloat(collisionBounds.width);
        out.writeFloat(collisionBounds.height);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        x = in.readFloat();
        y = in.readFloat();
        width = in.readFloat();
        height = in.readFloat();
        collisionBounds.set(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
    }
}
