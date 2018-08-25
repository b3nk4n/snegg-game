package de.bsautermeister.snegg.common;

public class GameManager implements Resettable {
    public static final GameManager INSTANCE = new GameManager();

    private GameState state = GameState.READY;

    private GameManager() {
        reset();
    }

    public GameState getState() {
        // FIXME either this getter or all the small isXXX getter
        return state;
    }

    public boolean isReady() {
        return state.isReady();
    }

    public boolean isPlaying() {
        return state.isPlaying();
    }

    public boolean isPaused() {
        return state.isPaused();
    }

    public boolean isGameOver() {
        return state.isGameOver();
    }

    public void setPlaying() {
        state = GameState.PLAYING;
    }

    public void setPaused() {
        state = GameState.PAUSED;
    }

    public void setGameOver() {
        if (!state.isGameOver()) {
            state = GameState.GAME_OVER;
        }
    }

    public void reset() {
        setPlaying();
    }
}
