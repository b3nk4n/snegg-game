package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.common.GameManager;
import de.bsautermeister.snegg.listeners.GameListener;
import de.bsautermeister.snegg.screen.menu.MenuScreen;

public class GameScreen extends ScreenAdapter {
    private final GameApp game;
    private final AssetManager assetManager;

    private GameRenderer renderer;
    private GameController controller;

    private final GameListener collisionListener;

    private Sound coinSound;
    private Sound loseSound;
    private Sound fruitSound;
    private Sound spawnFruitSound;

    public GameScreen(GameApp game) {
        this.game = game;
        this.assetManager = game.getAssetManager();

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
        };
    }

    @Override
    public void show() {
        coinSound = assetManager.get(AssetDescriptors.Sounds.COIN);
        loseSound = assetManager.get(AssetDescriptors.Sounds.LOSE);
        fruitSound = assetManager.get(AssetDescriptors.Sounds.FRUIT);
        spawnFruitSound = assetManager.get(AssetDescriptors.Sounds.FRUIT_SPAWN);

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
