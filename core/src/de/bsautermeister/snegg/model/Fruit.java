package de.bsautermeister.snegg.model;

import com.badlogic.gdx.math.Interpolation;

import de.bsautermeister.snegg.config.GameConfig;

public class Fruit extends GameObject implements Collectible {
    private boolean collected;
    private float lifetime;


    public Fruit() {
        setSize(GameConfig.COLLECTIBLE_SIZE, GameConfig.COLLECTIBLE_SIZE);
        reset();
    }

    @Override
    public void reset() {
        collected = true;
    }

    @Override
    public void update(float delta) {
        lifetime -= delta;

        if (lifetime <= 0) {
            collect();
        }
    }

    @Override
    public int getScore() {
        return Math.round(Interpolation.linear.apply(
                GameConfig.FRUIT_END_SCORE,
                GameConfig.FRUIT_START_SCORE,
                lifetime / GameConfig.FRUIT_LIFETIME));
    }

    @Override
    public boolean isCollected() {
        return collected;
    }

    @Override
    public void collect() {
        collected = true;
    }

    @Override
    public void release() {
        collected = false;
        lifetime = GameConfig.FRUIT_LIFETIME;
    }
}
