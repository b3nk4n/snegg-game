package de.bsautermeister.snegg.text;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bsautermeister.snegg.assets.Styles;
import de.bsautermeister.snegg.common.Resettable;
import de.bsautermeister.snegg.GameConfig;

public class AnimatedText extends Stage implements Resettable {
    private final BitmapFont font;
    private final GlyphLayout glyphLayout;
    private String text;
    private Array<Container<Label>> charContainers;

    public AnimatedText(Viewport viewport, Batch batch, Skin skin, int maxTextLength) {
        super(viewport, batch);
        this.font = skin.getFont(Styles.Fonts.BIG);

        // enable smooth scaling for BitmapFont
        font.getRegion().getTexture()
                .setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        charContainers = new Array<Container<Label>>(maxTextLength);
        glyphLayout = new GlyphLayout();

        for (int i = 0; i < maxTextLength; i++) {
            Container<Label> container = new Container<Label>(new Label("", skin));
            container.align(Align.left);
            container.setTransform(true);
            addActor(container);
            charContainers.add(container);
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

        float delay = getCharacterDelay(text);

        for (int i = 0; i < text.length(); i++) {
            Container<Label> character = charContainers.get(i);
            character.setScale(0f);
            character.setColor(0, 0, 0, 0);
            character.addAction(
                    Actions.parallel(
                            Actions.delay(delay * i,Actions.parallel(
                                    Actions.alpha(1, GameConfig.CHAR_ANIMATION_TIME),
                                    Actions.scaleTo(1, 1, GameConfig.CHAR_ANIMATION_TIME, Interpolation.swingOut))),
                            Actions.delay(GameConfig.TEXT_ANIMATION_DURATION - GameConfig.CHAR_ANIMATION_TIME ,Actions.parallel(
                                    Actions.alpha(0, GameConfig.CHAR_ANIMATION_TIME)))
                    ));
        }
    }

    public void dropText(StatusText statusText) {
        final float dropTextOffsetY = 50f;
        reset();
        prepareText(statusText);

        float delay = getCharacterDelay(text);

        for (int i = 0; i < text.length(); i++) {
            Container<Label> character = charContainers.get(i);
            float startX = character.getX();
            float startY = character.getY();

            character.setY(startY + dropTextOffsetY);
            character.setColor(0, 0, 0, 0);
            character.addAction(
                    Actions.parallel(
                            Actions.delay(delay * i,Actions.parallel(
                                    Actions.alpha(1, GameConfig.CHAR_ANIMATION_TIME),
                                    Actions.moveTo(startX, startY, GameConfig.CHAR_ANIMATION_TIME, Interpolation.swingOut)
                            )),
                            Actions.delay(GameConfig.TEXT_ANIMATION_DURATION - GameConfig.CHAR_ANIMATION_TIME ,Actions.parallel(
                                    Actions.alpha(0, GameConfig.CHAR_ANIMATION_TIME)))
                    ));
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
            Container<Label> character = charContainers.get(i);
            character.getActor().setText(String.valueOf(text.charAt(i)));
            character.setPosition(leftX + positions.get(i), bottomY);
            character.setOrigin(advances.get(i) / 2, character.getHeight() / 8);
        }
    }

    private static float getCharacterDelay(String text) {
        return (GameConfig.TEXT_ANIMATION_TIME - GameConfig.CHAR_ANIMATION_TIME) / text.length();
    }
}
