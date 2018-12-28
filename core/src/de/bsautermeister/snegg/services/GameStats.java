package de.bsautermeister.snegg.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameStats {
    private final Preferences prefs;

    private static final String KEY_FRUIT_COUNTER = "fruitCounter";

    public GameStats() {
        prefs = Gdx.app.getPreferences(GameStats.class.getName());
    }

    public void incrementFruitCounter() {
        int count = getFruitCounter();
        prefs.putInteger(KEY_FRUIT_COUNTER, count + 1);
        prefs.flush();
    }

    public int getFruitCounter() {
        return prefs.getInteger(KEY_FRUIT_COUNTER, 0);
    }
}
