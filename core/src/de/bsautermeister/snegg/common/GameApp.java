package de.bsautermeister.snegg.common;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class GameApp extends Game {
    private AssetManager assetManager;
    private SpriteBatch batch;

    @Override
    public void create() {
        assetManager = new AssetManager();
        batch = new SpriteBatch();
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
        batch.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
