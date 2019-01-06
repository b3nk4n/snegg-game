package de.bsautermeister.snegg.screen.menu;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bsautermeister.snegg.SneggGame;
import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.assets.RegionNames;
import de.bsautermeister.snegg.assets.Styles;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.GameConfig;
import de.bsautermeister.snegg.screen.ScreenBase;
import de.bsautermeister.snegg.screen.game.GameScreen;
import de.bsautermeister.snegg.screen.transition.ScreenTransitions;
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

        boolean canResumeGame = SneggGame.hasSavedData();
        createUI(canResumeGame);
    }

    private void createUI(boolean canResumeGame) {
        Table table = new Table(skin);
        table.defaults().pad(10);

        TextureRegion backgroundRegion = atlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        Image title = new Image(skin, RegionNames.TITLE);
        Container<Image> titleContainer = new Container<Image>(title);
        titleContainer.center();
        titleContainer.top();
        titleContainer.padTop(75f);
        titleContainer.setFillParent(true);
        titleContainer.pack();
        titleContainer.setTransform(true);
        titleContainer.setColor(1.0f, 1.0f, 1.0f, 0.0f);
        titleContainer.addAction(Actions.alpha(1f, 0.5f, Interpolation.fade));

        Button resumeOrPlayButton;
        if (canResumeGame) {
            resumeOrPlayButton = new Button(skin, Styles.Button.RESUME_BIG);
        } else {
            resumeOrPlayButton = new Button(skin, Styles.Button.PLAY_BIG);
        }

        resumeOrPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                play();
            }
        });
        resumeOrPlayButton.setTransform(true);
        resumeOrPlayButton.setOrigin(Align.center);
        resumeOrPlayButton.setScale(0f);
        resumeOrPlayButton.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.alpha(1.0f, 0.5f, Interpolation.fade),
                                Actions.scaleTo(1.1f, 1.1f, 0.5f, Interpolation.fade)),
                        Actions.forever(
                                Actions.sequence(
                                        Actions.scaleTo(1.0f, 1.0f, 3.0f, Interpolation.fade),
                                        Actions.scaleTo(1.1f, 1.1f, 3.0f, Interpolation.fade)))
                ));
        table.add(resumeOrPlayButton).row();

        if (SneggGame.getGameServiceManager().isSupported()) {
            Table nestedTable = new Table(skin);
            nestedTable.defaults().pad(10);
            Button leaderboardsButton = new Button(skin, Styles.Button.LEADERBOARDS);
            leaderboardsButton.setTransform(true);
            leaderboardsButton.setOrigin(Align.center);
            leaderboardsButton.setScale(0f);
            leaderboardsButton.addAction(
                    Actions.parallel(
                            Actions.alpha(1.0f, 0.5f, Interpolation.fade),
                            Actions.scaleTo(1f, 1f, 0.5f, Interpolation.fade))
            );
            leaderboardsButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    SneggGame.getGameServiceManager().showScore(Leaderboards.Keys.LEADERBOARD);
                }
            });
            nestedTable.add(leaderboardsButton);

            Button reviewsButton = new Button(skin, Styles.Button.REVIEWS);
            reviewsButton.setTransform(true);
            reviewsButton.setOrigin(Align.center);
            reviewsButton.setScale(0f);
            reviewsButton.addAction(
                    Actions.parallel(
                            Actions.alpha(1.0f, 0.5f, Interpolation.fade),
                            Actions.scaleTo(1f, 1f, 0.5f, Interpolation.fade))
            );
            reviewsButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    SneggGame.getGameServiceManager().rateGame();
                }
            });
            nestedTable.add(reviewsButton).padTop(80);

            Button achievementsButton = new Button(skin, Styles.Button.ACHIEVEMENTS);
            achievementsButton.setTransform(true);
            achievementsButton.setOrigin(Align.center);
            achievementsButton.setScale(0f);
            achievementsButton.setColor(1f, 1f, 1f, 0f);
            achievementsButton.addAction(
                    Actions.parallel(
                            Actions.alpha(1.0f, 0.5f, Interpolation.fade),
                            Actions.scaleTo(1f, 1f, 0.5f, Interpolation.fade))
            );
            achievementsButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    SneggGame.getGameServiceManager().showAchievements();
                }
            });
            nestedTable.add(achievementsButton).row();

            table.add(nestedTable).row();
        }

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            // only show quit button on Desktop
            Button quitButton = new Button(skin, Styles.Button.QUIT);
            quitButton.setTransform(true);
            quitButton.setOrigin(Align.center);
            quitButton.setScale(0f);
            quitButton.setColor(1f, 1f, 1f, 0f);
            quitButton.addAction(
                    Actions.parallel(
                            Actions.alpha(1.0f, 0.5f),
                            Actions.scaleTo(1.1f, 1.1f, 0.5f, Interpolation.fade))
                    );
            quitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    quit();
                }
            });
            table.add(quitButton).row();
        }

        table.center();
        table.setFillParent(true);
        table.pack();

        Table footerTable = new Table(skin);
        footerTable.bottom();
        Label createdByLabel = new Label("Created with love by",
                skin, Styles.Label.FOOTER);
        Label createrNameLabel = new Label("Vanessa Kan & Benjamin Sautermeister",
                skin, Styles.Label.FOOTER);
        Label musicByLabel = new Label("Music by",
                skin, Styles.Label.FOOTER);
        Label artistNameLabel = new Label("Scott Holmes",
                skin, Styles.Label.FOOTER);
        footerTable.add(createdByLabel).row();
        footerTable.add(createrNameLabel).padBottom(16).row();
        footerTable.add(musicByLabel).row();
        footerTable.add(artistNameLabel).row();
        footerTable.center();
        footerTable.bottom();
        footerTable.setFillParent(true);
        footerTable.pack();
        footerTable.setTransform(true);
        footerTable.setColor(1.0f, 1.0f, 1.0f, 0.0f);
        footerTable.addAction(Actions.alpha(1f, 0.5f, Interpolation.fade));

        stage.addActor(table);
        stage.addActor(titleContainer);
        stage.addActor(footerTable);
        stage.setDebugAll(GameConfig.DEBUG_MODE);
    }

    private void play() {
        setScreen(new GameScreen(getGame()), ScreenTransitions.FADE);
    }

    private void quit() {
        Gdx.app.exit();
    }

    @Override
    public void resume() {
        super.resume();
        SneggGame.getMusicPlayer().play();
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

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }
}
