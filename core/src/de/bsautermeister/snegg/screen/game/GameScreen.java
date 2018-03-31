package de.bsautermeister.snegg.screen.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;

import de.bsautermeister.snegg.SneggGame;
import de.bsautermeister.snegg.common.GameManager;
import de.bsautermeister.snegg.screen.menu.MenuScreen;

public class GameScreen extends ScreenAdapter {
    private final SneggGame game;
    private final AssetManager assetManager;

    private GameRenderer renderer;
    private GameController controller;

    public GameScreen(SneggGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        controller = new GameController();
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
