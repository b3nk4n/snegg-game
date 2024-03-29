package de.bsautermeister.snegg.screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.screen.transition.ScreenTransition;

public abstract class ScreenBase extends ScreenAdapter {
    private final GameApp game;

    public ScreenBase(GameApp game) {
        this.game = game;
    }

    public InputProcessor getInputProcessor() {
        // override in subclass when input processor has to be blocked while transition
        return null;
    }

    public void setScreen(ScreenBase screen) {
        this.game.setScreen(screen);
    }

    public void setScreen(ScreenBase screen, ScreenTransition transition) {
        this.game.setScreen(screen, transition);
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

    /**
     * Not applying the internal viewports of the screen or changing camera projection of the batch
     * can be required when rendering the screen to a frame buffer as render target.
     */
    public void render(float delta, boolean usedInFbo) {
        this.render(delta);
    }

    @Override
    public void hide() {
        dispose();
    }
}
