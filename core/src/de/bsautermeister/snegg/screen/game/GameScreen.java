package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;

import java.io.IOException;

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

    private final static String SAVE_DAT_FILENAME = "snegg.dat";

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

        if (true) { // TODO varaible: loadSaved ?
            // The user might have a previous game. If this is the case, load it
            tryLoad();
        } else {
            // Ensure that there is no old save, we don't want to load it, thus delete it
            deleteSave();
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
        //if (gameOverDone || gameMode != GAME_MODE_SCORE || scorer.getCurrentScore() == 0)
        //    return;

        final FileHandle handle = Gdx.files.local(SAVE_DAT_FILENAME);
        if (!BinarySerializer.write(controller, handle.write(false))) {
            // TODO exception handling?
        }
    }

    private boolean tryLoad() {
        final FileHandle handle = Gdx.files.local(SAVE_DAT_FILENAME);

        if (handle.exists()) {
            if (!BinarySerializer.read(controller, handle.read())) {
                // TODO exception handling?
            }

            deleteSave();
            return true;
        }
        return false;
    }

    private void deleteSave() {
        final FileHandle handle = Gdx.files.local(SAVE_DAT_FILENAME);
        if (handle.exists())
            handle.delete();
    }

    boolean hasSavedData() {
        return Gdx.files.local(SAVE_DAT_FILENAME).exists();
    }
}
