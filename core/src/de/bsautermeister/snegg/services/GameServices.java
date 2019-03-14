package de.bsautermeister.snegg.services;

import java.util.Map;

public interface GameServices extends OnlineServices, PlatformDependentService {
    long UNDEFINED_SCORE = -1;

    void start();
    void stop();

    void unlockAchievement(String key);
    Map<String, Boolean> loadAchievements(boolean forceReload);
    void loadAchievements(boolean forceReload, LoadAchievementsCallback callback);
    void submitScore(String leaderboardKey, long highScore);
    long loadCurrentHighscore(String leaderboardKey);
    void loadCurrentHighscore(String leaderboardKey, LoadHighscoreCallback callback);

    public interface LoadAchievementsCallback {
        void success(Map<String, Boolean> achievementsResult);
        void error(String message);
    }

    public interface LoadHighscoreCallback {
        void success(long scoreResult);
        void error(String message);
    }
}
