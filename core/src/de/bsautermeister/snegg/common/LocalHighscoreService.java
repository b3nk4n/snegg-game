package de.bsautermeister.snegg.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class LocalHighscoreService implements ScoreProvider, Resettable {

    private static final String HIGHSCORE_KEY = "highscore";

    private int score;
    private int displayScore;

    private int highscore;
    private int displayHighscore;

    private Preferences prefs;

    public LocalHighscoreService() {
        prefs = Gdx.app.getPreferences("SneggGame");
        highscore = prefs.getInteger(HIGHSCORE_KEY, 0);
        displayHighscore = highscore;
        reset();
    }

    @Override
    public void reset() {
        score = 0;
        displayScore = 0;
    }

    public void incrementScore(int delta) {
        score += delta;

        if (score > highscore) {
            highscore = score;
        }
    }

    public int getDisplayScore() {
        return displayScore;
    }

    public int getDisplayHighscore() {
        return displayHighscore;
    }

    public void updateDisplayScore(float delta) {
        if (displayScore < score) {
            displayScore = Math.min(score, displayScore + (int)(100 * delta));
        }

        if (displayHighscore < highscore) {
            displayHighscore = Math.min(highscore, displayHighscore + (int)(100 * delta));
        }
    }

    public void saveHighscore() {
        if (score < highscore) {
            return;
        }

        highscore = score;
        prefs.putInteger(HIGHSCORE_KEY, highscore);
        prefs.flush();
    }
}
