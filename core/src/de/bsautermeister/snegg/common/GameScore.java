package de.bsautermeister.snegg.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.serializer.BinarySerializable;

public class GameScore implements ScoreProvider, Resettable, BinarySerializable {

    //private static final String HIGHSCORE_KEY = "highscore";

    private long score;
    private long displayScore;

    public GameScore() {
        reset();
    }

    @Override
    public void reset() {
        score = 0L;
        displayScore = 0L;
    }

    public long incrementScore(long delta) {
        score += delta;
        return score;
    }

    @Override
    public long getDisplayScore() {
        return displayScore;
    }

    @Override
    public long getScore() {
        return score;
    }

    public void updateDisplayScore(float delta) {
        if (displayScore < score) {
            displayScore = Math.min(score, displayScore + (long)(100 * delta));
        }
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeLong(score);
        out.writeLong(displayScore);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        score = in.readLong();
        displayScore = in.readLong();
    }
}
