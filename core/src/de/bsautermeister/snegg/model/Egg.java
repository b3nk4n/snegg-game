package de.bsautermeister.snegg.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.GameConfig;

public class Egg extends GameObject implements Collectible {
    private boolean collected;

    public Egg() {
        setSize(GameConfig.COLLECTIBLE_SIZE, GameConfig.COLLECTIBLE_SIZE);
        reset();
    }

    @Override
    public void reset() {
        collected = true;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public int getScore() {
        return GameConfig.EGG_SCORE;
    }

    @Override
    public boolean isCollected() {
        return collected;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public void collect() {
        collected = true;
    }

    @Override
    public void release() {
        collected = false;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        super.write(out);
        out.writeBoolean(collected);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        super.read(in);
        collected = in.readBoolean();
    }
}
