package de.bsautermeister.snegg.services;

import java.util.Map;

public interface GameServices extends PlatformDependentService {
    public static final long UNDEFINED_SCORE = -1;

    void start();
    void stop();

    void signIn();
    void signOut();
    void rateGame();
    void unlockAchievement(String key);
    void showAchievements();
    Map<String, Boolean> loadAchievements(boolean forceReload);
    void submitScore(String leaderboardKey, long highScore);
    long loadCurrentHighscore(String leaderboardKey);
    void showScore(String leaderboardKey);
    boolean isSignedIn();
}
