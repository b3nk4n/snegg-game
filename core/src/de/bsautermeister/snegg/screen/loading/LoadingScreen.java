package de.bsautermeister.snegg.screen.loading;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bsautermeister.snegg.GameConfig;
import de.bsautermeister.snegg.SneggGame;
import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.assets.AssetPaths;
import de.bsautermeister.snegg.assets.RegionNames;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.screen.ScreenBase;
import de.bsautermeister.snegg.screen.menu.MenuScreen;
import de.bsautermeister.snegg.util.GdxUtils;

public class LoadingScreen extends ScreenBase {
    private static final Logger LOGGER = new Logger(LoadingScreen.class.getName(), GameConfig.LOG_LEVEL);

    private Stage stage;

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image screenBg;
    private Image loadingBg;

    private float startX, endX;
    private float percent;

    private Actor loadingBar;

    private boolean isLoadingFinished = false;

    public LoadingScreen(GameApp game) {
        super(game);
    }

    @Override
    public void show() {
        Viewport viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        // Tell the manager to load assets for the loading screen
        getAssetManager().load(AssetDescriptors.Atlas.LOADING);
        // Wait until they are finished loading
        getAssetManager().finishLoading();

        // Initialize the stage where we will place everything
        stage = new Stage(viewport, getBatch());

        // Get our texture atlas from the manager
        TextureAtlas atlas = getAsset(AssetDescriptors.Atlas.LOADING);

        // Grab the regions from the atlas and create some images
        logo = new Image(atlas.findRegion(RegionNames.LOADING_LOGO));
        loadingFrame = new Image(atlas.findRegion(RegionNames.LOADING_FRAME));
        loadingBarHidden = new Image(atlas.findRegion(RegionNames.LOADING_BAR_HIDDEN));
        screenBg = new Image(atlas.findRegion(RegionNames.LOADING_BACKGROUND));
        loadingBg = new Image(atlas.findRegion(RegionNames.LOADING_FRAME_BACKGROUND));

        // Add the loading bar animation
        Animation<TextureAtlas.AtlasRegion> anim = new Animation<>(0.05f, atlas.findRegions(RegionNames.LOADING_ANIMATION));
        anim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        loadingBar = new LoadingBar(anim);

        // Add all the actors to the stage
        stage.addActor(screenBg);
        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);

        // Add everything to be loaded, for instance:
        loadAssets();
    }

    private void loadAssets() {
        for (AssetDescriptor<?> assetDescriptor : AssetDescriptors.ALL) {
            getAssetManager().load(assetDescriptor);
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width , height, true);

        // Make the background fill the screen
        screenBg.setSize(stage.getWidth() , stage.getHeight() );

        // Place the logo in the middle of the screen on top of the loading bar
        logo.setX((stage.getWidth() - logo.getWidth()) / 2);
        logo.setY((stage.getHeight() - logo.getHeight()) / 2 + 80);
        logo.setOrigin(Align.center);

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

        // Place the loading bar at the same spot as the frame
        loadingBar.setX(loadingFrame.getX() + 15);
        loadingBar.setY(loadingFrame.getY() + 5);

        // Place the image that will hide the bar on top of the bar
        loadingBarHidden.setX(loadingBar.getX() + 35);
        loadingBarHidden.setY(loadingBar.getY() - 3);
        // The start position and how far to move the hidden loading bar
        startX = loadingFrame.getX();
        endX = loadingFrame.getWidth() - loadingBarHidden.getWidth();

        // The rest of the hidden bar
        loadingBg.setSize(loadingFrame.getWidth(), loadingFrame.getHeight());
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setY(loadingBarHidden.getY() - 2);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        GdxUtils.clearScreen(Color.BLACK);

        // Interpolate the percentage to make it more smooth
        percent = Interpolation.linear.apply(percent, getAssetManager().getProgress(), 0.05f);

        // Update positions (and size) to match the percentage
        loadingBarHidden.setX(startX + endX * percent);
        loadingBg.setX(loadingBarHidden.getX() + loadingBarHidden.getWidth());
        loadingBg.setWidth((loadingFrame.getWidth() - loadingBarHidden.getWidth()) * (1 - percent));
        loadingBg.invalidate();

        // Show the loading screen
        stage.act();
        stage.draw();

        if (getAssetManager().update() && percent > 0.99f && !isLoadingFinished) {
            isLoadingFinished = true;

            // loading game service content, as soon as the asses have been loaded, and the user is
            // logged in
            SneggGame.getGameServiceManager().refresh();

            setScreen(new MenuScreen(getGame()));
        }
    }

    @Override
    public void dispose() {
        // Dispose the loading assets as we no longer need them
        getAssetManager().unload(AssetPaths.Atlas.LOADING);

        stage.dispose();
    }

    @Override
    public void pause() {
        super.pause();

        LOGGER.debug("PAUSE");
    }

    @Override
    public void resume() {
        super.resume();

        LOGGER.debug("RESUME");
    }
}
