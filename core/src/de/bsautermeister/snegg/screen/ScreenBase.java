package de.bsautermeister.snegg.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import de.bsautermeister.snegg.common.GameApp;

public abstract class ScreenBase extends ScreenAdapter {
    private final GameApp game;

    public ScreenBase(GameApp game) {
        this.game = game;
    }
    public void setScreen(Screen screen) {
        this.game.setScreen(screen);
    }

    public GameApp getGame() {
        return game;
    }

    public SpriteBatch getBatch() {
        return game.getBatch();
    }

    public AssetManager getAssetManager() {
        return game.getAssetManager();
    }

    public <T> T getAsset(AssetDescriptor<T> assetDescriptor) {
        return getAssetManager().get(assetDescriptor);
    }

    @Override
    public void hide() {
        dispose();
    }
}
