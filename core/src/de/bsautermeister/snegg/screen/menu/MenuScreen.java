package de.bsautermeister.snegg.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.assets.RegionNames;
import de.bsautermeister.snegg.assets.Styles;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.screen.ScreenBase;
import de.bsautermeister.snegg.screen.game.GameScreen;
import de.bsautermeister.snegg.services.Leaderboards;
import de.bsautermeister.snegg.util.GdxUtils;

public class MenuScreen extends ScreenBase {
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;

    public MenuScreen(GameApp game) {
        super(game);
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, getBatch());
        skin = getAsset(AssetDescriptors.Skins.UI);
        atlas = getAsset(AssetDescriptors.Atlas.GAMEPLAY);

        Gdx.input.setInputProcessor(stage);
        Actor ui = createUI();
        stage.addActor(ui);
    }

    private Actor createUI() {
        Table table = new Table(skin);
        table.defaults().pad(10);

        TextureRegion backgroundRegion = atlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        Image title = new Image(skin, RegionNames.TITLE);
        table.add(title).row();

        Button playButton = new Button(skin, Styles.Button.PLAY);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                play();
            }
        });
        table.add(playButton).row();

        if (getGame().getGameServices().isSupported()) {
            Button leaderboardsButton = new Button(skin, Styles.Button.LEADERBOARDS);
            leaderboardsButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    getGameServices().showScore(Leaderboards.Keys.LEADERBOARD);
                }
            });
            table.add(leaderboardsButton).row();

            Button achievementsButton = new Button(skin, Styles.Button.ACHIEVEMENTS);
            achievementsButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    getGameServices().showAchievements();
                }
            });
            table.add(achievementsButton).row();

            Button reviewsButton = new Button(skin, Styles.Button.REVIEWS);
            reviewsButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    getGameServices().rateGame();
                }
            });
            table.add(reviewsButton).row();
        }

        Button quitButton = new Button(skin, Styles.Button.QUIT);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quit();
            }
        });
        table.add(quitButton);

        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private void play() {
        setScreen(new GameScreen(getGame()));
    }

    private void quit() {
        if (getGameServices().isSupported()) {
            getGameServices().signOut();
        }

        Gdx.app.exit();
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
