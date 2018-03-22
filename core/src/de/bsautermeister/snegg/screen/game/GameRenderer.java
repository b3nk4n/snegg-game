package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.util.GdxUtils;
import de.bsautermeister.snegg.util.ViewportUtils;
import de.bsautermeister.snegg.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {

    private final GameController controller;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private DebugCameraController debugCameraController;

    public GameRenderer(GameController controller) {
        this.controller = controller;
        init();
    }

    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        GdxUtils.clearScreen();

        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        renderer.circle(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y, 4, 30);

        renderer.end();

        renderDebug();
    }

    private void renderDebug() {
        ViewportUtils.drawGrid(viewport, renderer);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        ViewportUtils.debugPixelsPerUnit(viewport);
    }

    @Override
    public void dispose() {

    }
}
