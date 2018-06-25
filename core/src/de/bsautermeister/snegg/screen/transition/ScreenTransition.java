package de.bsautermeister.snegg.screen.transition;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

public interface ScreenTransition {
    float getDuration();
    float getInterpolatedPercentage(float progress);

    void render(SpriteBatch batch, Texture currentScreenTexture, Texture nextScreenTexture, float progress);
}
