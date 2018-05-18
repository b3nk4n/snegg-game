package de.bsautermeister.snegg.config;

import com.badlogic.gdx.utils.Logger;

public final class GameConfig {
    public static final int LOG_LEVEL = Logger.DEBUG;

    public static final float WIDTH = 800f;
    public static final float HEIGHT = 480f;

    public static final float HUD_WIDTH = 800f; // still world units!
    public static final float HUD_HEIGHT = 480f; // still world units!

    public static final float WORLD_WIDTH = 25.0f;
    public static final float WORLD_HEIGHT = 15.0f;

    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2f;
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2f;

    public static final float SNAKE_SIZE = 1f;
    public static final float MOVE_TIME = 0.4f;
    public static final float MIN_MOVE_TIME = 0.1f;
    public static final float SNAKE_SPEED = 1f;

    public static final float DIFFICULTY_LOWERING_MOVE_TIME_FACTOR = 0.00025f;

    public static final float COLLECTIBLE_SIZE = 1f;
    public static final int COIN_SCORE = 25;
    public static final int FRUIT_START_SCORE = 100;
    public static final int FRUIT_END_SCORE = 10;
    public static final int FRUIT_SPAWN_INTERVAL = 5;
    public static final float FRUIT_MIN_SPAWN_DELAY = 1f;
    public static final float FRUIT_MAX_SPAWN_DELAY = 5f;
    public static final float FRUIT_LIFETIME = 15f;

    private static final float Y_OFFSET = 2f;
    public static final float MAX_Y = WORLD_HEIGHT - Y_OFFSET;

    private GameConfig() { }
}
