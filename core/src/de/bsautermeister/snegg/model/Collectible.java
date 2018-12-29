package de.bsautermeister.snegg.model;

interface Collectible {
    int getScore();
    boolean isCollected();
    boolean isExpired();
    void collect();
    void release();
}
