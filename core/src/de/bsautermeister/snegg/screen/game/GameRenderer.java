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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bsautermeister.snegg.GameConfig;
import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.assets.RegionNames;
import de.bsautermeister.snegg.common.GameState;
import de.bsautermeister.snegg.model.BodyPart;
import de.bsautermeister.snegg.model.Egg;
import de.bsautermeister.snegg.model.GameObject;
import de.bsautermeister.snegg.model.Snake;
import de.bsautermeister.snegg.model.SnakeHead;
import de.bsautermeister.snegg.model.Worm;
import de.bsautermeister.snegg.screen.menu.GameOverOverlay;
import de.bsautermeister.snegg.screen.menu.PauseOverlay;
import de.bsautermeister.snegg.text.AnimatedText;
import de.bsautermeister.snegg.text.StatusTextQueue;
import de.bsautermeister.snegg.util.GdxUtils;
import de.bsautermeister.snegg.util.ViewportUtils;
import de.bsautermeister.snegg.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {
    private static final float PADDING = 24.0f;

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
    private TextureRegion backgroundOverlayRegion;
    private TextureRegion[][] nestsRegions;
    private TextureRegion headRegion;
    private TextureRegion headKilledRegion;
    private TextureRegion currentHappyRegion;
    private TextureRegion[] headHappyRegions;
    private TextureRegion[][] eggsRegions;
    private TextureRegion wormRegion;
    private TextureRegion wormScaredRegion;
    private TextureRegion wormHoleRegion;
    private AnimatedText animatedText;
    private Stage hudStage;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;

    private DebugCameraController debugCameraController;

    public GameRenderer(SpriteBatch batch, AssetManager assetManager, GameController controller) {
        this.batch = batch;
        this.assetManager = assetManager;
        this.controller = controller;
        init();
    }

    private void init() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        hudViewport = new StretchViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        renderer = new ShapeRenderer();

        font = assetManager.get(AssetDescriptors.Fonts.BIG);

        Skin skin = assetManager.get(AssetDescriptors.Skins.UI);
        layout = new GlyphLayout();

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.Atlas.GAMEPLAY);
        backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        backgroundOverlayRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND_OVERLAY);
        headRegion = gamePlayAtlas.findRegion(RegionNames.HEAD);
        headKilledRegion = gamePlayAtlas.findRegion(RegionNames.HEAD_KILLED);
        headHappyRegions = new TextureRegion[RegionNames.HEAD_HAPPY.length];
        for (int i = 0; i < RegionNames.HEAD_HAPPY.length; ++i) {
            headHappyRegions[i] = gamePlayAtlas.findRegion(RegionNames.HEAD_HAPPY[i]);
        }
        nestsRegions = new TextureRegion[RegionNames.NESTS.length][RegionNames.NESTS[0].length];
        for (int i = 0; i < RegionNames.NESTS.length; ++i) {
            for (int j = 0; j < RegionNames.NESTS[0].length; ++j) {
                nestsRegions[i][j] = gamePlayAtlas.findRegion(RegionNames.NESTS[i][j]);
            }
        }
        eggsRegions = new TextureRegion[RegionNames.EGGS.length][RegionNames.EGGS[0].length];
        for (int i = 0; i < RegionNames.EGGS.length; ++i) {
            for (int j = 0; j < RegionNames.EGGS[0].length; ++j) {
                eggsRegions[i][j] = gamePlayAtlas.findRegion(RegionNames.EGGS[i][j]);
            }
        }
        wormRegion = gamePlayAtlas.findRegion(RegionNames.WORM);
        wormScaredRegion = gamePlayAtlas.findRegion(RegionNames.WORM_SCARED);
        wormHoleRegion = gamePlayAtlas.findRegion(RegionNames.WORM_HOLE);

        pauseOverlay = new PauseOverlay(skin, controller.getCallback());
        gameOverOverlay = new GameOverOverlay(skin, controller.getCallback());

        animatedText = new AnimatedText(hudViewport, batch, skin, 32);

        hudStage = new Stage(hudViewport, batch);
        hudStage.addActor(pauseOverlay);
        hudStage.addActor(gameOverOverlay);
        hudStage.setDebugAll(GameConfig.DEBUG_MODE);

        Gdx.input.setInputProcessor(hudStage);

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    public void render(float delta, boolean usedInFbo) {
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        GdxUtils.clearScreen();

        renderGame(usedInFbo);
        renderHud();
        renderDebug();
    }

    private void renderGame(boolean usedInFbo) {
        if (!usedInFbo) {
            viewport.apply();
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        drawGame();
        
        batch.end();
    }

    private void drawGame() {
        drawBackground();
        drawEgg();
        drawWorm();
        drawSnake();
        drawOverlay();
    }

    private void drawBackground() {
        batch.draw(backgroundRegion, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
    }

    private void drawOverlay() {
        if (controller.getState().isPaused() || controller.getState().isGameOver()) {
            batch.draw(backgroundOverlayRegion, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        }
    }

    private void drawWorm() {
        Worm worm = controller.getWorm();
        if (!worm.isCollected()) {
            SnakeHead head = controller.getSnake().getHead();

            Vector2 lookDirection = new Vector2(
                    head.getCenterX() - worm.getCenterX(),
                    head.getCenterY() - worm.getCenterY());

            batch.draw(wormHoleRegion, worm.getX(), worm.getY(), worm.getWidth(), worm.getHeight());

            TextureRegion wormTexture;
            float diffX = head.getX() - worm.getX();
            float diffY = head.getY() - worm.getY();
            if (diffX * diffX + diffY * diffY > GameConfig.WORM_SCARED_DISTANCE_SQUARED) {
                wormTexture = wormRegion;
            } else {
                wormTexture = wormScaredRegion;
            }

            batch.draw(wormTexture,
                    worm.getX(), worm.getY(),
                    worm.getWidth() / 2, worm.getHeight() / 2,
                    worm.getWidth(), worm.getHeight(),
                    1.0f, 1.0f,
                    lookDirection.angleDeg() + 90f);
        }
    }

    private void drawEgg() {
        Egg egg = controller.getEgg();
        if (!egg.isCollected()) {
            batch.draw(
                    eggsRegions[egg.getEggIndex()][egg.getFaceIndex()],
                    egg.getX(), egg.getY(), egg.getWidth(), egg.getHeight());
        }
    }

    private final Vector2 previous = new Vector2();
    private final Vector2 current = new Vector2();
    private void drawSnake() {
        Snake snake = controller.getSnake();
        SnakeHead head = snake.getHead();

        Array<BodyPart> bodyParts= snake.getBodyParts();
        for (int i = bodyParts.size - 1; i >= 0; --i) {
            BodyPart currentObject = bodyParts.get(i);
            GameObject previousObject = (i > 0) ? bodyParts.get(i - 1) : head;

            previous.set(previousObject.getCenterX(), previousObject.getCenterY());
            current.set(currentObject.getCenterX(), currentObject.getCenterY());
            Vector2 direction = previous.sub(current);
            float rotation = direction.angleDeg() + 90f;

            if (direction.len2() >= 2f) {
                // hack: if a body part is more than 1 unit away, it must be on the other side,
                //       so we simply rotate it by 180° to point into the right direction
                rotation += 180f;
            }

            previous.set(current.x, current.y);

            float bodyX = currentObject.getX();
            float cloneX = getWorldWrapX(bodyX);
            float bodyY = currentObject.getY();
            float cloneY = getWorldWrapY(bodyY);

            TextureRegion nestRegion = nestsRegions[currentObject.getEggIndex()][currentObject.getFaceIndex()];
            if (cloneX != bodyX || cloneY != bodyY) {
                batch.draw(nestRegion,
                        cloneX, cloneY,
                        currentObject.getWidth() / 2, currentObject.getHeight() / 2,
                        currentObject.getWidth(), currentObject.getHeight(),
                        1f, 1f,
                        rotation);
            }
            batch.draw(nestRegion,
                    bodyX, bodyY,
                    currentObject.getWidth() / 2, currentObject.getHeight() / 2,
                    currentObject.getWidth(), currentObject.getHeight(),
                    1f, 1f,
                    rotation);
        }

        float headX = head.getX();
        float cloneX = getWorldWrapX(headX);
        float headY = head.getY();
        float cloneY = getWorldWrapY(headY);
        float headRotation = snake.getDirection().angle() + 90f;
        TextureRegion currentHeadRegion = getSnakeHeadRegion();
        if (cloneX != headX || cloneY != headY) {
            batch.draw(currentHeadRegion,
                    cloneX, cloneY,
                    head.getWidth() / 2f, head.getHeight() / 2f,
                    head.getWidth(), head.getHeight(),
                    1f, 1f,
                    headRotation);
        }
        batch.draw(currentHeadRegion,
                headX, headY,
                head.getWidth() / 2, head.getHeight() / 2,
                head.getWidth(), head.getHeight(),
                1f, 1f,
                headRotation);
    }

    private TextureRegion getSnakeHeadRegion() {
        if (controller.getState().isAnyGameOverState()) {
            currentHappyRegion = null;
            return headKilledRegion;
        }

        if (controller.getSnake().isHappy()) {
            if (currentHappyRegion != null) {
                return currentHappyRegion;
            }
            int randomIndex = MathUtils.random(headHappyRegions.length - 1);
            currentHappyRegion = headHappyRegions[randomIndex];
            return currentHappyRegion;
        }

        currentHappyRegion = null;
        return headRegion;
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

            StatusTextQueue statusTextQueue = controller.getStatusTextQueue();
            if (statusTextQueue.hasMessage()) {
                animatedText.dropText(statusTextQueue.consume());
            }

            animatedText.act();
            animatedText.draw();
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

        if (gameState.isGameOver()) {
            if (!gameOverOverlay.isVisible()) {
                gameOverOverlay.setVisible(true);
                gameOverOverlay.setScore(controller.getScoreProvider().getScore());
            } else {
                // workaround: do not act during the first frame, otherwise button event which triggered
                // this overlay to show are processed in the overlay, which could immediately close it again
                hudStage.act();
            }
            hudStage.draw();
        } else {
            gameOverOverlay.setVisible(false);
        }
    }

    private void drawHud() {
        String scoreString = String.valueOf(controller.getScoreProvider().getDisplayScore());
        layout.setText(font, scoreString);
        font.draw(batch, layout,
                hudViewport.getWorldWidth() - layout.width - PADDING,
                hudViewport.getWorldHeight() - PADDING);
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

        Egg egg = controller.getEgg();
        if (!egg.isCollected()) {
            Rectangle eggBounds = egg.getCollisionBounds();
            renderer.setColor(Color.BLUE);
            renderer.rect(eggBounds.getX(), eggBounds.getY(), eggBounds.getWidth(), eggBounds.getHeight());
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

    public InputProcessor getInputProcessor() {
        return hudStage;
    }
}
