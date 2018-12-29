package de.bsautermeister.snegg.model;

import com.badlogic.gdx.math.Interpolation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.GameConfig;

public class Worm extends GameObject implements Collectible {
    private boolean collected;
    private float lifetime;


    public Worm() {
        setSize(GameConfig.COLLECTIBLE_SIZE, GameConfig.COLLECTIBLE_SIZE);
        reset();
    }

    @Override
    public void reset() {
        collected = true;
    }

    @Override
    public void update(float delta) {
        lifetime -= delta;
    }

    @Override
    public int getScore() {
        return Math.round(Interpolation.linear.apply(
                GameConfig.WORM_END_SCORE,
                GameConfig.WORM_START_SCORE,
                lifetime / GameConfig.WORM_LIFETIME));
    }

    @Override
    public boolean isCollected() {
        return collected;
    }

    @Override
    public boolean isExpired() {
        return lifetime <= 0 && !collected;
    }

    @Override
    public void collect() {
        collected = true;
    }

    @Override
    public void release() {
        collected = false;
        lifetime = GameConfig.WORM_LIFETIME;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        super.write(out);
        out.writeBoolean(collected);
        out.writeFloat(lifetime);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        super.read(in);
        collected = in.readBoolean();
        lifetime = in.readFloat();
    }
}
