package de.bsautermeister.snegg.listeners;

public interface GameListener {
    void hitCoin(long score);
    void hitFruit(long score);
    void snakeChanged(int snakeSize, long score);
    void spawnFruit();
    void lose();
    void quit();
    void finishGame(long score);
}
