package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

import de.bsautermeister.snegg.GameConfig;
import de.bsautermeister.snegg.SneggGame;
import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.common.GameServiceManager;
import de.bsautermeister.snegg.listeners.GameListener;
import de.bsautermeister.snegg.screen.ScreenBase;
import de.bsautermeister.snegg.screen.menu.MenuScreen;
import de.bsautermeister.snegg.screen.transition.ScreenTransitions;
import de.bsautermeister.snegg.services.GameStats;

public class GameScreen extends ScreenBase {

    private GameRenderer renderer;
    private GameController controller;

    private final GameServiceManager gameServiceManager;
    private final GameListener gameListener;
    private final GameStats gameStats;

    private Sound[] collectSounds;
    private Sound loseSound;
    private Sound[] wormSpawnSounds;

    private boolean navigateToMenuScreen;

    public GameScreen(GameApp game) {
        super(game);

        gameServiceManager = SneggGame.getGameServiceManager();
        gameStats = new GameStats();

        gameListener = new GameListener() {
            @Override
            public void hitEgg(long score) {
                playRandomSound(collectSounds);
            }

            @Override
            public void hitWorm(long score) {
                gameStats.incrementWormCounter();
                playRandomSound(collectSounds);
            }

            @Override
            public void snakeChanged(int snakeSize, long score) {
                gameServiceManager.checkAndUnlockAchievement(gameStats.getWormCounter(), snakeSize);
            }

            @Override
            public void spawnWorm() {
                playRandomSound(wormSpawnSounds);
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

            private void playRandomSound(Sound[] sounds) {
                int rndIdx = MathUtils.random(sounds.length - 1);
                collectSounds[rndIdx].play();
            }
        };
    }

    @Override
    public void show() {

        collectSounds = new Sound[] {
            getAsset(AssetDescriptors.Sounds.COLLECT1),
            getAsset(AssetDescriptors.Sounds.COLLECT2),
            getAsset(AssetDescriptors.Sounds.COLLECT3),
            getAsset(AssetDescriptors.Sounds.COLLECT4)
        };
        loseSound = getAsset(AssetDescriptors.Sounds.LOSE);
        wormSpawnSounds = new Sound[] {
            getAsset(AssetDescriptors.Sounds.WORM_SPAWN1),
            getAsset(AssetDescriptors.Sounds.WORM_SPAWN2),
            getAsset(AssetDescriptors.Sounds.WORM_SPAWN3)
        };

        controller = new GameController(gameListener);
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

        // decrease audio when we enter the game screen
        SneggGame.getMusicPlayer().setVolume(GameConfig.MUSIC_IN_GAME_VOLUME, false);
    }

    @Override
    public void pause() {
        super.pause();

        controller.save();

        // increase audio when we leave the game screen
        SneggGame.getMusicPlayer().setVolume(GameConfig.MUSIC_VOLUME, false);
    }

    @Override
    public InputProcessor getInputProcessor() {
        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(renderer.getInputProcessor());
        inputs.addProcessor(controller.getInputProcessor());
        return inputs;
    }
}
