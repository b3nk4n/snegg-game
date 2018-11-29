package de.bsautermeister.snegg.listeners;

public interface GameListener {
    void hitCoin(int score);
    void hitFruit(int score);
    void spawnFruit();
    void lose();
    void quit();
    void finishGame(int score);
}
