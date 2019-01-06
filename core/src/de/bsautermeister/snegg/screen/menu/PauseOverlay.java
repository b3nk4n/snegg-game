package de.bsautermeister.snegg.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.bsautermeister.snegg.assets.RegionNames;
import de.bsautermeister.snegg.assets.Styles;

public class PauseOverlay extends Table {
    private final OverlayCallback callback;

    public PauseOverlay(Skin skin, OverlayCallback callback) {
        super(skin);
        this.callback = callback;
        this.setVisible(false);

        init();
    }

    private void init() {
        defaults().pad(20);

        Image titleImage = new Image(getSkin(), RegionNames.PAUSE);

        Table buttonTable = new Table(getSkin());
        buttonTable.defaults().pad(20);
        buttonTable.center();

        Button quitButton = new Button(getSkin(), Styles.Button.QUIT);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callback.quit();
            }
        });
        buttonTable.add(quitButton);

        Button resumeButton = new Button(getSkin(), Styles.Button.RESUME);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callback.resume();
            }
        });
        buttonTable.add(resumeButton);

        add(titleImage).row();
        add(buttonTable);

        center();
        setFillParent(true);
        pack();
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
