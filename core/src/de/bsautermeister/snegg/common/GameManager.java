package de.bsautermeister.snegg.common;

public class GameManager {
    public static final GameManager INSTANCE = new GameManager();

    private GameState state = GameState.READY;

    private GameManager() {}

    public boolean isReady() {
        return state.isReady();
    }

    public boolean isPlaying() {
        return state.isPlaying();
    }

    public boolean isGameOver() {
        return state.isGameOver();
    }

    public void setPlaying() {
        state = GameState.PLAYING;
    }

    public void setGameOver() {
        state = GameState.GAME_OVER;
    }
}
