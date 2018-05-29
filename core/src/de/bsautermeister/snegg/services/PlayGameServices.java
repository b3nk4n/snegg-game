package de.bsautermeister.snegg.services;

public interface PlayGameServices {
    void signIn();
    void signOut();
    void rateGame();
    void unlockAchievement(String key);
    void showAchievements();
    void submitScore(int highScore);
    void showScore();
    boolean isSignedIn();
}
