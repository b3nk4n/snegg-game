package de.bsautermeister.snegg.screen.game;

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
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.assets.RegionNames;
import de.bsautermeister.snegg.common.GameManager;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.model.BodyPart;
import de.bsautermeister.snegg.model.Coin;
import de.bsautermeister.snegg.model.Fruit;
import de.bsautermeister.snegg.model.Snake;
import de.bsautermeister.snegg.model.SnakeHead;
import de.bsautermeister.snegg.util.GdxUtils;
import de.bsautermeister.snegg.util.ViewportUtils;
import de.bsautermeister.snegg.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {
    private static final float PADDING = 20.0f;

    private final SpriteBatch batch;
    private final AssetManager assetManager;
    private final GameController controller;

    private OrthographicCamera camera;
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
        layout = new GlyphLayout();

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.Atlas.GAME_PLAY);
        backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        headRegion = gamePlayAtlas.findRegion(RegionNames.HEAD);
        bodyRegion = gamePlayAtlas.findRegion(RegionNames.BODY);
        coinRegion = gamePlayAtlas.findRegion(RegionNames.COIN);
        orangeRegion = gamePlayAtlas.findRegion(RegionNames.ORANGE);

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    public void render(float delta) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        GdxUtils.clearScreen();

        renderGame();
        renderHud();
        //renderDebug();
    }

    private void renderGame() {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        drawGame();
        
        batch.end();
    }

    private void drawGame() {
        batch.draw(backgroundRegion, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        Snake snake = controller.getSnake();
        for (BodyPart bodyPart : snake.getBodyParts()) {
            float bodyX = bodyPart.getX();
            float cloneX = getWorldWrapX(bodyX);
            float bodyY = bodyPart.getY();
            float cloneY = getWorldWrapY(bodyY);
            if (cloneX != bodyX || cloneY != bodyY) {
                batch.draw(headRegion, cloneX, cloneY, bodyPart.getWidth(), bodyPart.getHeight());
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
        batch.draw(headRegion, headX, headY, head.getWidth(), head.getHeight());

        Coin coin = controller.getCoin();
        if (!coin.isCollected()) {
            batch.draw(coinRegion, coin.getX(), coin.getY(), coin.getWidth(), coin.getHeight());
        }

        Fruit fruit = controller.getFruit();
        if (!fruit.isCollected()) {
            batch.draw(orangeRegion, fruit.getX(), fruit.getY(), fruit.getWidth(), fruit.getHeight());
        }
    }

    private float getWorldWrapX(float headX) {
        float cloneX = headX;
        if (headX < 0) {
            cloneX = headX + GameConfig.WORLD_WIDTH;
        } else if (headX > GameConfig.WORLD_WIDTH - GameConfig.SNAKE_SIZE) {
            cloneX = headX - GameConfig.WORLD_WIDTH;
        }
        return cloneX;
    }

    private float getWorldWrapY(float headY) {
        float cloneY = headY;
        if (headY < 0) {
            cloneY = headY + GameConfig.MAX_Y;
        } else if (headY > GameConfig.MAX_Y - GameConfig.SNAKE_SIZE) {
            cloneY = headY - GameConfig.MAX_Y;
        }
        return cloneY;
    }

    private void renderHud() {
        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.begin();

        drawHud();

        batch.end();
    }

    private void drawHud() {
        String highscoreString = "HIGHSCORE: " + GameManager.INSTANCE.getDisplayHighScore();
        layout.setText(font, highscoreString);
        font.draw(batch, layout, PADDING, hudViewport.getWorldHeight() - PADDING);

        float scoreX = hudViewport.getWorldWidth() - layout.width;
        float scoreY = hudViewport.getWorldHeight() - PADDING;

        String scoreString = "SCORE: " + GameManager.INSTANCE.getDisplayScore();
        font.draw(batch, scoreString, scoreX, scoreY);
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
}
