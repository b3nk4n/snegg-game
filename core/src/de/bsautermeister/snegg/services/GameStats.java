package de.bsautermeister.snegg.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameStats {
    private final Preferences prefs;

    private static final String KEY_WORM_COUNTER = "wormCounter";

    public GameStats() {
        prefs = Gdx.app.getPreferences(GameStats.class.getName());
    }

    public void incrementWormCounter() {
        int count = getWormCounter();
        prefs.putInteger(KEY_WORM_COUNTER, count + 1);
        prefs.flush();
    }

    public int getWormCounter() {
        return prefs.getInteger(KEY_WORM_COUNTER, 0);
    }
}
