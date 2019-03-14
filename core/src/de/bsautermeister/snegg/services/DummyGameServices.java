package de.bsautermeister.snegg.services;

import java.util.HashMap;
import java.util.Map;

public class DummyGameServices implements GameServices {
    @Override
    public void start() {
        // NOP
    }

    @Override
    public void stop() {
        // NOP
    }

    @Override
    public boolean isSupported() {
        return false;
    }

    @Override
    public void signIn() {
        // NOP
    }

    @Override
    public void signOut() {
        // NOP
    }

    @Override
    public void rateGame() {
        // NOP
    }

    @Override
    public void unlockAchievement(String key) {
        // NOP
    }

    @Override
    public void showAchievements() {
        // NOP
    }

    @Override
    public Map<String, Boolean> loadAchievements(boolean forceReload) {
        return new HashMap<String, Boolean>();
    }

    @Override
    public void loadAchievements(boolean forceReload, LoadAchievementsCallback callback) {
        callback.error("Not implemented");
    }

    @Override
    public void submitScore(String leaderboardKey, long highScore) {
        // NOP
    }

    @Override
    public long loadCurrentHighscore(String leaderboardKey) {
        return UNDEFINED_SCORE;
    }

    @Override
    public void loadCurrentHighscore(String leaderboardKey, LoadHighscoreCallback callback) {
        callback.error("Not implemented");
    }

    @Override
    public void showScore(String leaderboardKey) {
        // NOP
    }

    @Override
    public boolean isSignedIn() {
        return false;
    }
}
