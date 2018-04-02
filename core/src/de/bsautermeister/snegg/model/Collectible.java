package de.bsautermeister.snegg.model;

interface Collectible {
    int getScore();
    boolean isCollected();
    void collect();
    void free();
}
