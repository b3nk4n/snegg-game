package de.bsautermeister.snegg.model;

import de.bsautermeister.snegg.GameConfig;

public class SnakeHead extends SmoothGameObject {

    public SnakeHead() {
        super(GameConfig.INITIAL_TRANS_DURATION, GameConfig.INITIAL_TRANS_DELAY);
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        gotoXY((int)(GameConfig.GAMEFIELD_WIDTH / 2), 3);
    }
}
