package de.bsautermeister.snegg.common;

public enum GameState {
    READY,
    PLAYING,
    PAUSED,
    GAME_OVER;

    public boolean isReady() {
        return this == READY;
    }

    public boolean isPlaying() {
        return this == PLAYING;
    }

    public boolean isPaused() {
        return this == PAUSED;
    }

    public boolean isGameOver() {
        return this == GAME_OVER;
    }
}
