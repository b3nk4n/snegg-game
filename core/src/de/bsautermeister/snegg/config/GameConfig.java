package de.bsautermeister.snegg.config;

import com.badlogic.gdx.utils.Logger;

public final class GameConfig {
    public static final int LOG_LEVEL = Logger.DEBUG;
    public static final boolean DEBUG_MODE = false;

    public static final float WIDTH = 720;
    public static final float HEIGHT = 1280;

    public static final float HUD_WIDTH = 720; // still world units!
    public static final float HUD_HEIGHT = 1280; // still world units!

    public static final float WORLD_WIDTH = 9.0f;
    public static final float WORLD_HEIGHT = 16.0f;

    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2f;
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2f;

    public static final float SNAKE_SIZE = 1f;
    public static final float MOVE_TIME = 0.40f;
    public static final float MIN_MOVE_TIME = 0.1f;
    public static final float SNAKE_SPEED = 1f;

    public static final float INITIAL_TRANS_DURATION = MOVE_TIME / 2;
    public static final float INITIAL_TRANS_DELAY = INITIAL_TRANS_DURATION / 5;

    public static final float DIFFICULTY_LOWERING_MOVE_TIME_FACTOR = 0.00025f;

    public static final float COLLECTIBLE_SIZE = 1f;
    public static final int COIN_SCORE = 25;
    public static final int FRUIT_START_SCORE = 100;
    public static final int FRUIT_END_SCORE = 10;
    public static final int FRUIT_SPAWN_INTERVAL = 5;
    public static final float FRUIT_MIN_SPAWN_DELAY = 1f;
    public static final float FRUIT_MAX_SPAWN_DELAY = 5f;
    public static final float FRUIT_LIFETIME = 15f;

    private static final float MARGIN_TOP = 0f;
    private static final float MARGIN_OTHER = 0f;

    public static final float MIN_X = MARGIN_OTHER;
    public static final float MAX_X = WORLD_WIDTH - MARGIN_OTHER;
    public static final float MIN_Y = MARGIN_OTHER;
    public static final float MAX_Y = WORLD_HEIGHT - MARGIN_TOP;
    public static final float GAMEFIELD_WIDTH = MAX_X - MIN_X;
    public static final float GAMEFIELD_HEIGHT = MAX_Y - MIN_Y;

    private GameConfig() { }
}
