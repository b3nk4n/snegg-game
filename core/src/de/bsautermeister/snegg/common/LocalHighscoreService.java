package de.bsautermeister.snegg.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.serializer.BinarySerializable;

public class LocalHighscoreService implements ScoreProvider, Resettable, BinarySerializable {

    private static final String HIGHSCORE_KEY = "highscore";

    private int score;
    private int displayScore;

    private int highscore;

    private Preferences prefs;

    public LocalHighscoreService() {
        prefs = Gdx.app.getPreferences("SneggGame");
        highscore = prefs.getInteger(HIGHSCORE_KEY, 0);
        reset();
    }

    @Override
    public void reset() {
        score = 0;
        displayScore = 0;
    }

    public void incrementScore(int delta) {
        score += delta;
    }

    @Override
    public int getDisplayScore() {
        return displayScore;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getHighscore() {
        return highscore;
    }

    public void updateDisplayScore(float delta) {
        if (displayScore < score) {
            displayScore = Math.min(score, displayScore + (int)(100 * delta));
        }
    }

    public void saveHighscore() {
        if (score <= highscore) {
            return;
        }

        highscore = score;
        prefs.putInteger(HIGHSCORE_KEY, highscore);
        prefs.flush();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(score);
        out.writeInt(displayScore);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        score = in.readInt();
        displayScore = in.readInt();
    }
}
