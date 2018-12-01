package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Logger;

import de.bsautermeister.snegg.SneggGame;
import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.audio.MusicPlayer;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.common.GameServiceManager;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.listeners.GameListener;
import de.bsautermeister.snegg.screen.ScreenBase;
import de.bsautermeister.snegg.screen.menu.MenuScreen;
import de.bsautermeister.snegg.screen.transition.ScreenTransitions;
import de.bsautermeister.snegg.services.Leaderboards;

public class GameScreen extends ScreenBase {
    private static final Logger LOGGER = new Logger(GameScreen.class.getName(), GameConfig.LOG_LEVEL);

    private GameRenderer renderer;
    private GameController controller;

    private final GameServiceManager gameServiceManager;
    private final GameListener collisionListener;

    private Sound coinSound;
    private Sound loseSound;
    private Sound fruitSound;
    private Sound spawnFruitSound;

    private boolean navigateToMenuScreen;

    public GameScreen(GameApp game) {
        super(game);

        gameServiceManager = new GameServiceManager(game.getGameServices());

        collisionListener = new GameListener() {
            @Override
            public void hitCoin(long score) {
                coinSound.play();
            }

            @Override
            public void hitFruit(long score) {
                fruitSound.play();
            }

            @Override
            public void snakeChanged(int snakeSize, long score) {
                String unlockedAchievement = gameServiceManager.checkAndUnlockAchievement(0, snakeSize);
                publishAchievementMessage(unlockedAchievement);
            }

            private void publishAchievementMessage(String unlockedAchievement) {
                if (unlockedAchievement.equals("")) {
                    return;
                }

                controller.publishMessage(unlockedAchievement);
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

            @Override
            public void finishGame(long score) {
                gameServiceManager.submitScore(score);
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
            controller.load();
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
    public void resume() {
        super.resume();
        LOGGER.debug("RESUME");

        MusicPlayer.getInstance().setVolume(GameConfig.MUSIC_IN_GAME_VOLUME, false);
    }

    @Override
    public void pause() {
        super.pause();
        LOGGER.debug("PAUSE");

        MusicPlayer.getInstance().setVolume(GameConfig.MUSIC_VOLUME, false);
        controller.save();
    }

    @Override
    public InputProcessor getInputProcessor() {
        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(renderer.getInputProcessor());
        inputs.addProcessor(controller.getInputProcessor());
        return inputs;
    }
}
