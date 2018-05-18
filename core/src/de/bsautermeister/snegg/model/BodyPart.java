package de.bsautermeister.snegg.model;

import de.bsautermeister.snegg.config.GameConfig;

public class BodyPart extends SmoothGameObject {
    private boolean justAdded;

    public BodyPart(float transitionDelay) {
        super(GameConfig.MOVE_TIME / 2f, transitionDelay);
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
