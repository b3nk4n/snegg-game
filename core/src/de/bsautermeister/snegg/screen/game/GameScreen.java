package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.audio.Sound;

import de.bsautermeister.snegg.assets.AssetDescriptors;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.common.GameManager;
import de.bsautermeister.snegg.common.GameServiceApp;
import de.bsautermeister.snegg.listeners.GameListener;
import de.bsautermeister.snegg.screen.ScreenBase;
import de.bsautermeister.snegg.screen.menu.MenuScreen;

public class GameScreen extends ScreenBase {
    private GameRenderer renderer;
    private GameController controller;

    private final GameListener collisionListener;

    private Sound coinSound;
    private Sound loseSound;
    private Sound fruitSound;
    private Sound spawnFruitSound;

    private final GameServiceApp gameServiceApp;

    public GameScreen(GameServiceApp game) {
        super(game);

        this.gameServiceApp = game; // TODO fix getGame() vs. this.gameServiceApp (redundant vs. close coupling to PlayServices in GameApp base class)

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
        coinSound = getAsset(AssetDescriptors.Sounds.COIN);
        loseSound = getAsset(AssetDescriptors.Sounds.LOSE);
        fruitSound = getAsset(AssetDescriptors.Sounds.FRUIT);
        spawnFruitSound = getAsset(AssetDescriptors.Sounds.FRUIT_SPAWN);

        controller = new GameController(collisionListener);
        renderer = new GameRenderer(getBatch(), getAssetManager(), controller);
    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.render(delta);

        if (GameManager.INSTANCE.isGameOver()) {
            setScreen(new MenuScreen(getGame()));
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
}
