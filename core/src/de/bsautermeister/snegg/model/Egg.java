package de.bsautermeister.snegg.model;

import com.badlogic.gdx.math.MathUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.GameConfig;
import de.bsautermeister.snegg.assets.RegionNames;

public class Egg extends GameObject implements Collectible {
    private boolean collected;

    private int eggIndex;
    private int faceIndex;

    public Egg() {
        setSize(GameConfig.COLLECTIBLE_SIZE, GameConfig.COLLECTIBLE_SIZE);
        reset();
    }

    @Override
    public void reset() {
        collected = true;

        eggIndex = MathUtils.random(RegionNames.EGGS.length - 1);
        faceIndex = MathUtils.random(RegionNames.EGGS[0].length - 1);
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

    public int getEggIndex() {
        return eggIndex;
    }

    public int getFaceIndex() {
        return faceIndex;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        super.write(out);
        out.writeBoolean(collected);
        out.writeInt(eggIndex);
        out.writeInt(faceIndex);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        super.read(in);
        collected = in.readBoolean();
        eggIndex = in.readInt();
        faceIndex = in.readInt();
    }
}
