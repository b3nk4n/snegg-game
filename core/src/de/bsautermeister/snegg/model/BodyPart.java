package de.bsautermeister.snegg.model;

import de.bsautermeister.snegg.config.GameConfig;

public class BodyPart extends GameObject {
    private boolean justAdded;

    public BodyPart() {
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
        justAdded = true;
    }

    public boolean isJustAdded() {
        return justAdded;
    }

    public void setJustAdded(boolean justAdded) {
        this.justAdded = justAdded;
    }
}
