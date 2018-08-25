package de.bsautermeister.snegg.model;

import de.bsautermeister.snegg.config.GameConfig;

public class BodyPart extends SmoothGameObject {
    private boolean justAdded;

    public BodyPart() {
        super(0.0f, 0.0f); // will be set at runtime
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        justAdded = true;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    public boolean isJustAdded() {
        return justAdded;
    }

    public void setJustAdded(boolean justAdded) {
        this.justAdded = justAdded;
    }
}
