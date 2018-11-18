package de.bsautermeister.snegg.model;

import com.badlogic.gdx.utils.Queue;

public class StatusTextQueue {
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
}
