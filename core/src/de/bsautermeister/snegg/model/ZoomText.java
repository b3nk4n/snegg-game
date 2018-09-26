package de.bsautermeister.snegg.model;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import de.bsautermeister.snegg.common.Resettable;

public class ZoomText implements Resettable {
    private String text;
    private Vector2 center;
    private float startScale = 1.0f;
    private float endScale;
    private float scale;
    private float totalLifetime;
    private float remainingLifetime;
    private final Interpolation interpolation;

    public ZoomText(Interpolation interpolation) {
        this.interpolation = interpolation;
    }

    @Override
    public void reset() {
        this.remainingLifetime = 0;
    }

    public void show(String text, Vector2 center, float startScale, float endScale, float duration) {
        if (startScale <= 0 || duration <= 0) {
            throw new IllegalArgumentException("Illegal zoom text params");
        }

        this.text = text;
        this.center = center;
        this.startScale = startScale;
        this.scale = startScale;
        this.endScale = endScale;
        this.totalLifetime = duration;
        this.remainingLifetime = duration;
    }

    public void update(float delta) {
        if (!isActive()) {
            return;
        }

        this.remainingLifetime = Math.max(0f, remainingLifetime - delta);
        float progress = 1f - this.remainingLifetime / this.totalLifetime;
        this.scale = this.interpolation.apply(startScale, endScale, progress);
    }

    public boolean isActive() {
        return this.remainingLifetime > 0;
    }

    public String getText() {
        return text;
    }

    public Vector2 getCenter() {
        return center;
    }

    public float getScale() {
        return scale;
    }
}
