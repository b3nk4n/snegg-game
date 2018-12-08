package de.bsautermeister.snegg.common;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.bsautermeister.snegg.screen.ScreenBase;
import de.bsautermeister.snegg.screen.transition.ScreenTransition;


public abstract class GameApp implements ApplicationListener {
    private AssetManager assetManager;
    private SpriteBatch batch;

    private TransitionContext transitionContext;

    @Override
    public void create() {
        assetManager = new AssetManager();
        batch = new SpriteBatch();

        transitionContext = new TransitionContext(batch);
    }

    public void setScreen(ScreenBase screen) {
        setScreen(screen, null);
    }

    public void setScreen(ScreenBase screen, ScreenTransition transtion) {
        transitionContext.setScreen(screen, transtion);
    }

    public ScreenBase getScreen() {
        return transitionContext.getScreen();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        transitionContext.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        transitionContext.resize(width, height);
    }

    @Override
    public void pause() {
        transitionContext.pause();
    }

    @Override
    public void resume() {
        transitionContext.resume();
    }

    @Override
    public void dispose() {
        transitionContext.dispose();

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
