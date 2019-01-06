package de.bsautermeister.snegg.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.GameConfig;

public class BodyPart extends SmoothGameObject {
    private boolean justAdded;

    private int eggIndex;
    private int faceIndex;

    public BodyPart() {
        super(0.0f, 0.0f); // will be set at runtime
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        justAdded = true;
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
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        super.read(in);
        justAdded = in.readBoolean();
        eggIndex = in.readInt();
        faceIndex = in.readInt();
    }
}
