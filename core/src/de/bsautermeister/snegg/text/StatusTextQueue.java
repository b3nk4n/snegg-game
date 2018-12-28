package de.bsautermeister.snegg.text;

import com.badlogic.gdx.utils.Queue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.snegg.serializer.BinarySerializable;

public class StatusTextQueue implements BinarySerializable {
    private Queue<StatusText> messageQueue;
    private boolean readyToShow;

    private final float minMessageDelay;
    private float delayTimer;

    public StatusTextQueue(float minMessageDelay) {
        this.minMessageDelay = minMessageDelay;
        messageQueue = new Queue<StatusText>(4);
    }

    public void publish(StatusText statusText) {
        messageQueue.addFirst(statusText);
    }

    public void update(float delta) {
        delayTimer = Math.max(0f, delayTimer - delta);
    }

    public boolean hasMessage() {
        return delayTimer <= 0f && messageQueue.size > 0;
    }

    public StatusText consume() {
        if (!hasMessage()) {
            throw new IllegalStateException("Not allowed to consume a message when hasMessage() is false.");
        }

        delayTimer += minMessageDelay;
        return messageQueue.removeLast();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(messageQueue.size);
        for (StatusText statusText : messageQueue) {
            out.writeUTF(statusText.getText());
            out.writeFloat(statusText.getX());
            out.writeFloat(statusText.getY());
        }
        out.writeBoolean(readyToShow);
        out.writeFloat(delayTimer);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        int messageSize = in.readInt();
        for (int i = 0; i < messageSize; i++) {
            messageQueue.addFirst(
                    new StatusText(in.readUTF(), in.readFloat(), in.readFloat()));
        }
        readyToShow = in.readBoolean();
        delayTimer = in.readFloat();
    }
}
