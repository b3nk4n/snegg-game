package de.bsautermeister.snegg.services;

public interface GameServices extends PlatformDependentService {
    void start();
    void stop();

    void signIn();
    void signOut();
    void rateGame();
    void unlockAchievement(String key);
    void showAchievements();
    void submitScore(String leaderboardKey, int highScore);
    void showScore(String leaderboardKey);
    boolean isSignedIn();
}
