package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.assets.RegionNames;
import de.bsautermeister.snegg.common.GameState;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.model.BodyPart;
import de.bsautermeister.snegg.model.Coin;
import de.bsautermeister.snegg.model.Fruit;
import de.bsautermeister.snegg.model.Snake;
import de.bsautermeister.snegg.model.SnakeHead;
import de.bsautermeister.snegg.screen.menu.PauseOverlay;
import de.bsautermeister.snegg.util.GdxUtils;
import de.bsautermeister.snegg.util.ViewportUtils;
import de.bsautermeister.snegg.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {
    private static final float PADDING = 20.0f;

    private final SpriteBatch batch;
    private final AssetManager assetManager;
    private final GameController controller;

    private static OrthographicCamera camera;
    private Viewport viewport;
    private Viewport hudViewport;
    private ShapeRenderer renderer;

    private BitmapFont font;
    private GlyphLayout layout;

    private TextureRegion backgroundRegion;
    private TextureRegion bodyRegion;
    private TextureRegion headRegion;
    private TextureRegion coinRegion;
    private TextureRegion orangeRegion;

    private Skin skin;
    private Stage hudStage;
    private PauseOverlay pauseOverlay;

    private DebugCameraController debugCameraController;

    public GameRenderer(SpriteBatch batch, AssetManager assetManager, GameController controller) {
        this.batch = batch;
        this.assetManager = assetManager;
        this.controller = controller;
        init();
    }

    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        renderer = new ShapeRenderer();

        font = assetManager.get(AssetDescriptors.Fonts.UI);
        skin = assetManager.get(AssetDescriptors.Skins.UI);
        layout = new GlyphLayout();

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.Atlas.GAMEPLAY);
        backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        headRegion = gamePlayAtlas.findRegion(RegionNames.HEAD);
        bodyRegion = gamePlayAtlas.findRegion(RegionNames.BODY);
        coinRegion = gamePlayAtlas.findRegion(RegionNames.COIN);
        orangeRegion = gamePlayAtlas.findRegion(RegionNames.ORANGE);

        pauseOverlay = new PauseOverlay(skin, controller.getCallback());

        hudStage = new Stage(hudViewport, batch);
        hudStage.addActor(pauseOverlay);
        hudStage.setDebugAll(GameConfig.DEBUG_MODE);

        Gdx.input.setInputProcessor(hudStage);

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        GdxUtils.clearScreen();

        renderGame();
        renderHud();
        renderDebug();
    }

    private void renderGame() {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        drawGame();
        
        batch.end();
    }

    private void drawGame() {
        drawBackground();
        drawCoin();
        drawFruit();
        drawSnake();
    }

    private void drawBackground() {
        batch.draw(backgroundRegion, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
    }

    private void drawFruit() {
        Fruit fruit = controller.getFruit();
        if (!fruit.isCollected()) {
            batch.draw(orangeRegion, fruit.getX(), fruit.getY(), fruit.getWidth(), fruit.getHeight());
        }
    }

    private void drawCoin() {
        Coin coin = controller.getCoin();
        if (!coin.isCollected()) {
            batch.draw(coinRegion, coin.getX(), coin.getY(), coin.getWidth(), coin.getHeight());
        }
    }

    private void drawSnake() {
        Snake snake = controller.getSnake();
        for (BodyPart bodyPart : snake.getBodyParts()) {
            float bodyX = bodyPart.getX();
            float cloneX = getWorldWrapX(bodyX);
            float bodyY = bodyPart.getY();
            float cloneY = getWorldWrapY(bodyY);
            if (cloneX != bodyX || cloneY != bodyY) {
                batch.draw(bodyRegion, cloneX, cloneY, bodyPart.getWidth(), bodyPart.getHeight());
            }
            batch.draw(bodyRegion, bodyPart.getX(), bodyPart.getY(), bodyPart.getWidth(), bodyPart.getHeight());
        }

        SnakeHead head = snake.getHead();

        float headX = head.getX();
        float cloneX = getWorldWrapX(headX);
        float headY = head.getY();
        float cloneY = getWorldWrapY(headY);
        if (cloneX != headX || cloneY != headY) {
            batch.draw(headRegion, cloneX, cloneY, head.getWidth(), head.getHeight());
        }
        batch.draw(headRegion,
                headX, headY,
                head.getWidth() / 2, head.getHeight() / 2,
                head.getWidth(), head.getHeight(),
                1f, 1f,
                snake.getDirection().angle() + 90f);
    }

    private float getWorldWrapX(float headX) {
        float cloneX = headX;
        if (headX < GameConfig.MIN_X) {
            cloneX = headX + GameConfig.GAMEFIELD_WIDTH;
        } else if (headX > GameConfig.MAX_X - GameConfig.SNAKE_SIZE) {
            cloneX = headX - GameConfig.GAMEFIELD_WIDTH;
        }
        return cloneX;
    }

    private float getWorldWrapY(float headY) {
        float cloneY = headY;
        if (headY < GameConfig.MIN_Y) {
            cloneY = headY + GameConfig.GAMEFIELD_HEIGHT;
        } else if (headY > GameConfig.MAX_Y - GameConfig.SNAKE_SIZE) {
            cloneY = headY - GameConfig.GAMEFIELD_HEIGHT;
        }
        return cloneY;
    }

    private void renderHud() {
        hudViewport.apply();

        GameState gameState = controller.getState();

        if (gameState.isPlaying()) {
            batch.setProjectionMatrix(hudViewport.getCamera().combined);
            batch.begin();

            drawHud();

            batch.end();
        }

        if (gameState.isPaused()) {
            if (!pauseOverlay.isVisible()) {
                pauseOverlay.setVisible(true);
            } else {
                // workaround: do not act during the first frame, otherwise button event which triggered
                // this overlay to show are processed in the overlay, which could immediately close it again
                hudStage.act();
            }
            hudStage.draw();
        } else {
            pauseOverlay.setVisible(false);
        }
    }

    private void drawHud() {
        String highscoreString = "HIGHSCORE: " + controller.getScoreProvider().getDisplayHighscore();
        layout.setText(font, highscoreString);
        font.draw(batch, layout, PADDING, hudViewport.getWorldHeight() - PADDING);

        float scoreX = hudViewport.getWorldWidth() - layout.width;
        float scoreY = hudViewport.getWorldHeight() - PADDING;

        String scoreString = "SCORE: " + controller.getScoreProvider().getDisplayScore();
        font.draw(batch, scoreString, scoreX, scoreY);
    }

    private void renderDebug() {
        if (!GameConfig.DEBUG_MODE) {
            return;
        }

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
        if (!coin.isCollected()) {
            Rectangle coinBounds = coin.getCollisionBounds();
            renderer.setColor(Color.BLUE);
            renderer.rect(coinBounds.getX(), coinBounds.getY(), coinBounds.getWidth(), coinBounds.getHeight());
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        ViewportUtils.debugPixelsPerUnit(viewport);
        ViewportUtils.debugPixelsPerUnit(hudViewport);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        font.dispose();
    }

    public static Vector3 projectToWorld(float x, float y) {
        return camera.unproject(new Vector3(x, y, 0));
    }

    public static Vector3 projectToScreen(float x, float y) {
        return camera.project(new Vector3(x, y, 0));
    }

    public InputProcessor getInputProcessor() {
        return hudStage;
    }
}
