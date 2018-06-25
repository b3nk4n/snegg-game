package de.bsautermeister.snegg.screen.transition;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

import de.bsautermeister.snegg.util.GdxUtils;

public class ScaleScreenTransition extends ScreenTransitionBase {

    private boolean scaleOut;
    private Interpolation interpolation;

    public ScaleScreenTransition(float duration) {
        this(duration, false);
    }

    public ScaleScreenTransition(float duration, boolean scaleOut) {
        this(duration, scaleOut, Interpolation.linear);
    }

    public ScaleScreenTransition(float duration, boolean scaleOut, Interpolation interpolation) {
        super(duration);

        if (interpolation == null) {
            throw new IllegalArgumentException("Interpolation function is required");
        }

        this.scaleOut = scaleOut;
        this.interpolation = interpolation;
    }

    @Override
    public void render(SpriteBatch batch, Texture currentScreenTexture, Texture nextScreenTexture, float progress) {
        progress = interpolation.apply(progress);

        float scale = scaleOut ? progress : 1 - progress;

        // draw order depends on scale type (in / out)
        Texture topTexture = scaleOut ? nextScreenTexture : currentScreenTexture;
        Texture bottomTexture = scaleOut ? currentScreenTexture : nextScreenTexture;

        int topTextureWidth = topTexture.getWidth();
        int topTextureHeight = topTexture.getHeight();
        int bottomTextureWidth = bottomTexture.getWidth();
        int bottomTextureHeight = bottomTexture.getHeight();

        // drawing
        GdxUtils.clearScreen();
        batch.begin();

        batch.draw(bottomTexture,
                0, 0,
                0, 0,
                bottomTextureWidth, bottomTextureHeight,
                1, 1,
                0,
                0, 0,
                bottomTextureWidth, bottomTextureHeight,
                false, true);

        batch.draw(topTexture,
                0, 0,
                topTextureWidth / 2f, topTextureHeight / 2f,
                topTextureWidth, topTextureHeight,
                scale, scale,
                0,
                0, 0,
                topTextureWidth, topTextureHeight,
                false, true);

        batch.end();
    }
}
