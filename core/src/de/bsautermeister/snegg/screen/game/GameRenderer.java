package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.model.BodyPart;
import de.bsautermeister.snegg.model.Coin;
import de.bsautermeister.snegg.model.Snake;
import de.bsautermeister.snegg.model.SnakeHead;
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

        renderDebug();
    }

    private void renderDebug() {
        ViewportUtils.drawGrid(viewport, renderer);

        viewport.apply();
        Color oldColor = new Color(renderer.getColor());
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        renderer.end();
        renderer.setColor(oldColor);
    }

    private void drawDebug() {
        Snake snake = controller.getSnake();

        for (BodyPart bodyPart : snake.getBodyParts()) {
            renderer.setColor(Color.YELLOW);
            Rectangle bodyBounds = bodyPart.getCollisionBounds();
            renderer.rect(bodyBounds.getX(), bodyBounds.getY(), bodyBounds.getWidth(), bodyBounds.getHeight());
        }

        SnakeHead head = snake.getHead();
        Rectangle headBounds = head.getCollisionBounds();
        renderer.setColor(Color.GREEN);
        renderer.rect(headBounds.getX(), headBounds.getY(), headBounds.getWidth(), headBounds.getHeight());

        Coin coin = controller.getCoin();
        Rectangle coinBounds = coin.getCollisionBounds();
        renderer.setColor(Color.BLUE);
        renderer.rect(coinBounds.getX(), coinBounds.getY(), coinBounds.getWidth(), coinBounds.getHeight());
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        ViewportUtils.debugPixelsPerUnit(viewport);
    }

    @Override
    public void dispose() {

    }
}
