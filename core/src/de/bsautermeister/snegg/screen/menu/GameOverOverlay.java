package de.bsautermeister.snegg.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.bsautermeister.snegg.assets.RegionNames;
import de.bsautermeister.snegg.assets.Styles;

public class GameOverOverlay extends Table {
    private static final String SCORE_TEXT = "SCORE: ";

    private final OverlayCallback callback;
    private Label scoreLabel;

    public GameOverOverlay(Skin skin, OverlayCallback callback) {
        super(skin);
        this.callback = callback;
        this.setVisible(false);

        init();
    }

    private void init() {
        defaults().pad(20);

        Image titleImage = new Image(getSkin(), RegionNames.GAME_OVER);

        scoreLabel = new Label(SCORE_TEXT, getSkin());
        Table buttonTable = new Table(getSkin());
        buttonTable.defaults().pad(20);
        buttonTable.center();

        Button resumeButton = new Button(getSkin(), Styles.Button.PLAY);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callback.restart();
            }
        });
        buttonTable.add(resumeButton);

        Button quitButton = new Button(getSkin(), Styles.Button.QUIT);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callback.quit();
            }
        });
        buttonTable.add(quitButton);

        add(titleImage).row();
        add(scoreLabel).row();
        add(buttonTable);

        center();
        setFillParent(true);
        pack();
    }

    public void setScore(long score) {
        scoreLabel.setText(SCORE_TEXT + score);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
            Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            callback.resume();
        }
    }
}
