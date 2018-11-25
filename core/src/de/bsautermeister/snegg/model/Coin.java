package de.bsautermeister.snegg.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.serializer.BinarySerializable;

public class Coin extends GameObject implements Collectible {
    private boolean collected;

    public Coin() {
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
        return GameConfig.COIN_SCORE;
    }

    @Override
    public boolean isCollected() {
        return collected;
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
