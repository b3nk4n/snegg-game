package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.common.GameManager;
import de.bsautermeister.snegg.listeners.CollisionListener;
import de.bsautermeister.snegg.screen.menu.MenuScreen;

public class GameScreen extends ScreenAdapter {
    private final GameApp game;
    private final AssetManager assetManager;

    private GameRenderer renderer;
    private GameController controller;

    private final CollisionListener collisionListener;

    private Sound coinSound;
    private Sound loseSound;

    public GameScreen(GameApp game) {
        this.game = game;
        this.assetManager = game.getAssetManager();

        collisionListener = new CollisionListener() {
            @Override
            public void hitCoin() {
                coinSound.play();
            }

            @Override
            public void hitFruit() {
                // TODO play fruit hit sound
            }

            @Override
            public void lose() {
                loseSound.play();
            }
        };
    }

    @Override
    public void show() {
        coinSound = assetManager.get(AssetDescriptors.Sounds.COIN);
        loseSound = assetManager.get(AssetDescriptors.Sounds.LOSE);

        controller = new GameController(collisionListener);
        renderer = new GameRenderer(game.getBatch(), assetManager, controller);
    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.render(delta);

        if (GameManager.INSTANCE.isGameOver()) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
