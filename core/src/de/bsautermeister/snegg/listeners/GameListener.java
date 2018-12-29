package de.bsautermeister.snegg.listeners;

public interface GameListener {
    void hitEgg(long score);
    void hitWorm(long score);
    void snakeChanged(int snakeSize, long score);
    void spawnWorm();
    void lose();
    void quit();
    void finishGame(long score);
}
