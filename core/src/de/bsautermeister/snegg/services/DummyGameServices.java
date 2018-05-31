package de.bsautermeister.snegg.services;

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
    public void submitScore(String leaderboardKey, int highScore) {
        // NOP
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
