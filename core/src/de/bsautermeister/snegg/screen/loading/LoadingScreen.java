package de.bsautermeister.snegg.screen.loading;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bsautermeister.snegg.SneggGame;
import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.screen.game.GameScreen;
import de.bsautermeister.snegg.util.GdxUtils;

public class LoadingScreen extends ScreenAdapter {
    private static final float PROGESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2f;
    private static final float PROGESS_BAR_HEIGHT = 66f;

    private final SneggGame game;
    private final AssetManager assetManager;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private float progress;
    private float waitTime = 0.75f;

    private boolean changeToGameScreen;

    public LoadingScreen(SneggGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        loadAssets();
    }

    private void loadAssets() {
        assetManager.load(AssetDescriptors.UI_FONT);
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();

        update(delta);

        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        draw();
        renderer.end();

        checkChangeScreen();
    }

    private void update(float delta) {
        progress = assetManager.getProgress();

        if (assetManager.update()) {
            waitTime -= delta;

            if (waitTime <= 0) {
                changeToGameScreen = true;
            }
        }
    }

    private void checkChangeScreen() {
        if (changeToGameScreen) {
            game.setScreen(new GameScreen(game));
        }
    }

    private void draw() {
        renderer.rect(
                (GameConfig.HUD_WIDTH - PROGESS_BAR_WIDTH) / 2f,
                (GameConfig.HUD_HEIGHT - PROGESS_BAR_HEIGHT) / 2f,
                progress * PROGESS_BAR_WIDTH,
                PROGESS_BAR_HEIGHT);


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
