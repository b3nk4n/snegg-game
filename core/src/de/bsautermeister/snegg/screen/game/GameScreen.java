package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;

import de.bsautermeister.snegg.SneggGame;
import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.listeners.GameListener;
import de.bsautermeister.snegg.screen.ScreenBase;
import de.bsautermeister.snegg.screen.menu.MenuScreen;
import de.bsautermeister.snegg.screen.transition.ScreenTransitions;
import de.bsautermeister.snegg.serializer.BinarySerializer;

public class GameScreen extends ScreenBase {
    private static final Logger LOGGER = new Logger(GameScreen.class.getName(), GameConfig.LOG_LEVEL);

    private GameRenderer renderer;
    private GameController controller;

    private final GameListener collisionListener;

    private Sound coinSound;
    private Sound loseSound;
    private Sound fruitSound;
    private Sound spawnFruitSound;

    private boolean navigateToMenuScreen;

    public GameScreen(GameApp game) {
        super(game);

        collisionListener = new GameListener() {
            @Override
            public void hitCoin() {
                coinSound.play();
            }

            @Override
            public void hitFruit() {
                fruitSound.play();
            }

            @Override
            public void spawnFruit() {
                spawnFruitSound.play();
            }

            @Override
            public void lose() {
                loseSound.play();
            }

            @Override
            public void quit() {
                navigateToMenuScreen = true;
            }
        };
    }

    @Override
    public void show() {
        coinSound = getAsset(AssetDescriptors.Sounds.COIN);
        loseSound = getAsset(AssetDescriptors.Sounds.LOSE);
        fruitSound = getAsset(AssetDescriptors.Sounds.FRUIT);
        spawnFruitSound = getAsset(AssetDescriptors.Sounds.FRUIT_SPAWN);

        controller = new GameController(collisionListener);
        renderer = new GameRenderer(getBatch(), getAssetManager(), controller);

        if (SneggGame.hasSavedData()) {
            tryLoad();
            // ensure to not load this saved game later anymore
            SneggGame.deleteSavedData();
        }

    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.render(delta);

        if (navigateToMenuScreen) {
            setScreen(new MenuScreen(getGame()), ScreenTransitions.FADE);
        }
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    @Override
    public void pause() {
        super.pause();

        LOGGER.debug("PAUSE");

        save();
    }

    @Override
    public void resume() {
        super.resume();

        LOGGER.debug("RESUME");
    }

    @Override
    public InputProcessor getInputProcessor() {
        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(renderer.getInputProcessor());
        inputs.addProcessor(controller.getInputProcessor());
        return inputs;
    }

    private void save() {
        if (controller.getState().isAnyGameOverState()) {
            // don't save the game, when the player is game over, otherwise he would resume the game
            // which would end immediately afterwards
            return;
        }

        final FileHandle handle = SneggGame.getSavedDataHandle();
        if (!BinarySerializer.write(controller, handle.write(false))) {
            // TODO exception handling?
        }
    }

    private boolean tryLoad() {
        final FileHandle handle = SneggGame.getSavedDataHandle();

        if (handle.exists()) {
            if (!BinarySerializer.read(controller, handle.read())) {
                // TODO exception handling?
            }

            SneggGame.deleteSavedData();
            return true;
        }
        return false;
    }
}
