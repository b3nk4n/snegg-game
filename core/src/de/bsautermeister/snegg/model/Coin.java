package de.bsautermeister.snegg.model;

import de.bsautermeister.snegg.config.GameConfig;

public class Coin extends GameObject implements Collectible {
    private boolean collected;

    public Coin() {
        setSize(GameConfig.COIN_SIZE, GameConfig.COIN_SIZE);
        reset();
    }

    public void reset() {
        collected = true;
    }

    @Override
    public int getScore() {
        return GameConfig.COIN_SCORE;
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
    public void free() {
        collected = false;
    }
}
