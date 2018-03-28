package de.bsautermeister.snegg.model;

import de.bsautermeister.snegg.config.GameConfig;

public class Coin extends GameObject {
    private boolean available;

    public Coin() {
        setSize(GameConfig.COIN_SIZE, GameConfig.COIN_SIZE);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
