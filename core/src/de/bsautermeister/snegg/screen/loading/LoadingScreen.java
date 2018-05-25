package de.bsautermeister.snegg.screen.loading;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.assets.RegionNames;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.screen.menu.MenuScreen;
import de.bsautermeister.snegg.util.GdxUtils;

public class LoadingScreen extends ScreenAdapter {
    private final static String LOADING_ATLAS = "loading/loading.atlas";

    private final GameApp game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image screenBg;
    private Image loadingBg;

    private float startX, endX;
    private float percent;

    private Actor loadingBar;

    public LoadingScreen(GameApp game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        // Tell the manager to load assets for the loading screen

        assetManager.load(AssetDescriptors.Atlas.LOADING);
        // Wait until they are finished loading
        assetManager.finishLoading();

        // Initialize the stage where we will place everything
        stage = new Stage(viewport, game.getBatch());

        // Get our texture atlas from the manager
        TextureAtlas atlas = assetManager.get(AssetDescriptors.Atlas.LOADING);

        // Grab the regions from the atlas and create some images
        logo = new Image(atlas.findRegion(RegionNames.LOADING_LOGO));
        loadingFrame = new Image(atlas.findRegion(RegionNames.LOADING_FRAME));
        loadingBarHidden = new Image(atlas.findRegion(RegionNames.LOADING_BAR_HIDDEN));
        screenBg = new Image(atlas.findRegion(RegionNames.LOADING_BACKGROUND));
        loadingBg = new Image(atlas.findRegion(RegionNames.LOADING_FRAME_BACKGROUND));

        // Add the loading bar animation
        Animation anim = new Animation(0.05f, atlas.findRegions("loading-bar-anim") );
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
        assetManager.load(AssetDescriptors.Fonts.UI);
        assetManager.load(AssetDescriptors.Atlas.GAMEPLAY);
        assetManager.load(AssetDescriptors.Skins.UI);
        assetManager.load(AssetDescriptors.Sounds.COIN);
        assetManager.load(AssetDescriptors.Sounds.LOSE);
        assetManager.load(AssetDescriptors.Sounds.FRUIT);
        assetManager.load(AssetDescriptors.Sounds.FRUIT_SPAWN);
    }

    @Override
    public void resize(int width, int height) {
        // Set our screen to always be XXX x 480 in size
        stage.getViewport().update(width , height, true);

        // Make the background fill the screen
        screenBg.setSize(width, height);
        // Place the logo in the middle of the screen and 100 px up
        logo.setX((stage.getWidth() - logo.getWidth()) / 2);
        logo.setY((stage.getHeight() - logo.getHeight()) / 2 + 100);

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

        // Place the loading bar at the same spot as the frame, adjusted a few px
        loadingBar.setX(loadingFrame.getX() + 15);
        loadingBar.setY(loadingFrame.getY() + 5);

        // Place the image that will hide the bar on top of the bar, adjusted a few px
        loadingBarHidden.setX(loadingBar.getX() + 35);
        loadingBarHidden.setY(loadingBar.getY() - 3);
        // The start position and how far to move the hidden loading bar
        startX = loadingBarHidden.getX();
        endX = 440;

        // The rest of the hidden bar
        loadingBg.setSize(450, 50);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setY(loadingBarHidden.getY() + 3);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        GdxUtils.clearScreen();

        // Interpolate the percentage to make it more smooth
        percent = Interpolation.linear.apply(percent, assetManager.getProgress(), 0.05f);

        // Update positions (and size) to match the percentage
        loadingBarHidden.setX(startX + endX * percent);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setWidth(450 - 450 * percent);
        loadingBg.invalidate();

        // Show the loading screen
        stage.act();
        stage.draw();

        if (assetManager.update() && percent > 0.99f) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        // Dispose the loading assets as we no longer need them
        assetManager.unload(LOADING_ATLAS);

        stage.dispose();
    }
}
