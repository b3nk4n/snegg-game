package de.bsautermeister.snegg.services;

import java.util.Map;

public interface GameServices extends OnlineServices, PlatformDependentService {
    long UNDEFINED_SCORE = -1;

    void start();
    void stop();

    void unlockAchievement(String key);
    Map<String, Boolean> loadAchievements(boolean forceReload);
    void submitScore(String leaderboardKey, long highScore);
    long loadCurrentHighscore(String leaderboardKey);
}
