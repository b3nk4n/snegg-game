package de.bsautermeister.snegg.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.FloatArray;

import de.bsautermeister.snegg.common.Resettable;
import de.bsautermeister.snegg.config.GameConfig;

public class AnimatedText extends Stage implements Resettable {
    private final BitmapFont font;
    private final GlyphLayout glyphLayout;
    private final TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
    private String text;
    private TextButton[] characters; // we use TextButton instead of Label, because Label needs a wrapping container to set transform to TRUE for scaling.

    public AnimatedText(BitmapFont font, int maxTextLength) {
        this.font = font;

        style.font = font;

        font.getRegion().getTexture()
                .setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        characters = new TextButton[maxTextLength];
        glyphLayout = new GlyphLayout();

        for (int i = 0; i < maxTextLength; i++) {
            characters[i] = new TextButton("", style);
            characters[i].setTransform(true);
            addActor(characters[i]);
        }

        reset();
    }

    private FloatArray getPositions(String text, GlyphLayout.GlyphRun run) {
        float[] positions = new float[text.length()];

        for (int i = 0; i < positions.length; i++) {
            if (i == 0) {
                positions[0] = run.xAdvances.get(0);
            } else {
                positions[i] = positions[i - 1] + run.xAdvances.get(i);
            }
        }

        return FloatArray.with(positions);
    }

    @Override
    public void reset() {
        this.text = "";
    }

    public void appearText(StatusText statusText) {
        reset();
        prepareText(statusText);

        float leftX = statusText.getX() - glyphLayout.width / 2;
        float bottomY = statusText.getY() + glyphLayout.height / 2;

        float delay = getCharacterDelay(text);

        for (int i = 0; i < text.length(); i++) {
            characters[i].setScale(0f);
            characters[i].setColor(0, 0, 0, 0);
            characters[i].addAction(
                    Actions.delay(delay * i,Actions.parallel(
                            Actions.alpha(1, GameConfig.CHAR_ANIMATION_TIME),
                            Actions.scaleTo(1, 1, GameConfig.CHAR_ANIMATION_TIME, Interpolation.swingOut)
                    )));
        }
    }

    public void dropText(StatusText statusText) {
        final float dropTextOffsetY = 50f;
        reset();
        prepareText(statusText);

        float delay = getCharacterDelay(text);

        for (int i = 0; i < text.length(); i++) {
            float startX = characters[i].getX();
            float startY = characters[i].getY();

            characters[i].setY(startY + dropTextOffsetY);
            characters[i].setColor(0, 0, 0, 0);
            characters[i].addAction(
                    Actions.delay(delay * i,Actions.parallel(
                            Actions.alpha(1, GameConfig.CHAR_ANIMATION_TIME),
                            Actions.moveTo(startX, startY, GameConfig.CHAR_ANIMATION_TIME, Interpolation.swingOut)
                    )));
        }
    }

    private void prepareText(StatusText statusText) {
        this.text = statusText.getText();

        glyphLayout.setText(font, text);
        GlyphLayout.GlyphRun run = glyphLayout.runs.get(0);
        FloatArray advances = run.xAdvances;
        FloatArray positions = getPositions(text, run);

        float leftX = statusText.getX() - glyphLayout.width / 2;
        float bottomY = statusText.getY() - glyphLayout.height / 2;

        for (int i = 0; i < text.length(); i++) {
            characters[i].setPosition(leftX + positions.get(i), bottomY);
            characters[i].setOrigin(advances.get(i) / 2, characters[i].getHeight() / 4);
            characters[i].setText(String.valueOf(text.charAt(i)));
        }
    }

    private static float getCharacterDelay(String text) {
        return (GameConfig.TEXT_ANIMATION_TIME - GameConfig.CHAR_ANIMATION_TIME) / text.length();
    }
}
