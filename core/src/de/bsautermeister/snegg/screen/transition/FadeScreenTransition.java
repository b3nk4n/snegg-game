package de.bsautermeister.snegg.screen.transition;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

import de.bsautermeister.snegg.util.GdxUtils;

public class FadeScreenTransition extends ScreenTransitionBase {

    public FadeScreenTransition(float duration) {
        super(duration);
    }

    @Override
    public void render(SpriteBatch batch, Texture currentScreenTexture, Texture nextScreenTexture, float progress) {
        int currentScreenWidth = currentScreenTexture.getWidth();
        int currentScreenHeight = currentScreenTexture.getHeight();

        int nextScreenWidth = nextScreenTexture.getWidth();
        int nextScreenHeight = nextScreenTexture.getHeight();

        // interpolate progress
        progress = Interpolation.fade.apply(progress);

        GdxUtils.clearScreen();

        Color oldColor = batch.getColor().cpy();

        batch.begin();

        // draw current screen
        batch.setColor(1, 1, 1, 1f - progress);
        batch.draw(currentScreenTexture,
                0, 0,
                0, 0,
                currentScreenWidth, currentScreenHeight,
                1, 1,
                0,
                0, 0,
                currentScreenWidth, currentScreenHeight,
                false, true); // flip y-axis because buffer is y-axis is downward

        // draw next screen
        batch.setColor(1, 1, 1, progress);
        batch.draw(nextScreenTexture,
                0, 0,
                0, 0,
                nextScreenWidth, nextScreenHeight,
                1, 1,
                0,
                0, 0,
                nextScreenWidth, nextScreenHeight,
                false, true);

        batch.end();
    }
}
