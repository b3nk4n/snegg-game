package de.bsautermeister.snegg.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import de.bsautermeister.snegg.SneggGame;

public class GameManager {
    public static final GameManager INSTANCE = new GameManager();

    private static final String HIGHSCORE_KEY = "highscore";

    private GameState state = GameState.READY;

    private int score;
    private int displayScore;
    private int highScore;
    private int displayHighScore;

    private Preferences prefs;

    private GameManager() {
        prefs = Gdx.app.getPreferences(SneggGame.class.getSimpleName());
        highScore = prefs.getInteger(HIGHSCORE_KEY, 0);
        displayHighScore = highScore;
        reset();
    }

    public boolean isReady() {
        return state.isReady();
    }

    public boolean isPlaying() {
        return state.isPlaying();
    }

    public boolean isGameOver() {
        return state.isGameOver();
    }

    public void setPlaying() {
        state = GameState.PLAYING;
    }

    public void setGameOver() {
        state = GameState.GAME_OVER;
    }

    public int getDisplayScore() {
        return displayScore;
    }

    public int getDisplayHighScore() {
        return displayHighScore;
    }

    public void incrementScore(int delta) {
        score += delta;

        if (score > highScore) {
            highScore = score;
        }
    }

    public void reset() {
        setPlaying();

        score = 0;
        displayScore = 0;
    }

    public void updateDisplayScore(float delta) {
        if (displayScore < score) {
            displayScore = Math.min(score, displayScore + (int)(100 * delta));
        }

        if (displayHighScore < highScore) {
            displayHighScore = Math.min(highScore, displayHighScore + (int)(100 * delta));
        }
    }

    public void saveHighscore() {
        if (score < highScore) {
            return;
        }

        highScore = score;
        prefs.putInteger(HIGHSCORE_KEY, highScore);
        prefs.flush();
    }
}
