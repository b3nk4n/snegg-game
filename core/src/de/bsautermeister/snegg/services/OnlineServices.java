package de.bsautermeister.snegg.services;

import java.util.Map;

public interface OnlineServices {
    void signIn();
    boolean isSignedIn();
    void signOut();
    void rateGame();
    void showAchievements();
    void showScore(String leaderboardKey);
}
