package de.bsautermeister.snegg.model;

import com.badlogic.gdx.math.MathUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.GameConfig;

public class BodyPart extends SmoothGameObject {
    private boolean justAdded;

    private int eggIndex;
    private int faceIndex;
    private float rotationOffset; // either 0, 90, 180 or 270;

    public BodyPart() {
        super(0.0f, 0.0f); // will be set at runtime
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        justAdded = true;

        rotationOffset = MathUtils.random(3) * 90f;
    }

    public void setIdentity(int eggIndex, int faceIndex) {
        this.eggIndex = eggIndex;
        this.faceIndex = faceIndex;
    }

    public void setFaceIndex(int faceIndex) {
        this.faceIndex = faceIndex;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    public boolean isJustAdded() {
        return justAdded;
    }

    public void setJustAdded(boolean justAdded) {
        this.justAdded = justAdded;
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
        out.writeBoolean(justAdded);
        out.writeInt(eggIndex);
        out.writeInt(faceIndex);
        out.writeFloat(rotationOffset);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        super.read(in);
        justAdded = in.readBoolean();
        eggIndex = in.readInt();
        faceIndex = in.readInt();
        rotationOffset = in.readFloat();
    }
}
