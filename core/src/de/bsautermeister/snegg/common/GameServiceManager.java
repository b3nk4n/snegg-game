package de.bsautermeister.snegg.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Logger;

import java.util.HashMap;
import java.util.Map;

import de.bsautermeister.snegg.GameConfig;
import de.bsautermeister.snegg.services.Achievements;
import de.bsautermeister.snegg.services.GameServices;
import de.bsautermeister.snegg.services.Leaderboards;
import de.bsautermeister.snegg.services.OnlineServices;
import de.bsautermeister.snegg.services.PlatformDependentService;

public class GameServiceManager implements OnlineServices, PlatformDependentService {
    private static final Logger LOG = new Logger(GameServiceManager.class.getSimpleName(), GameConfig.LOG_LEVEL);

    private static final String HIGHSCORE_FALLBACK_KEY = "highscore";

    private final GameServices gameServices;

    private final Preferences prefs;
    private long onlineHighscore = GameServices.UNDEFINED_SCORE;
    private Map<String, Boolean> onlineAchievements = new HashMap<String, Boolean>();

    public GameServiceManager(GameServices gameServices) {
        this.gameServices = gameServices;
        this.prefs = Gdx.app.getPreferences(GameServiceManager.class.getName());
    }

    public void refresh() {
        refreshUserHighscore();
        refreshAchievements();
    }

    private void refreshUserHighscore() {
        final long highscoreFallback = prefs.getLong(HIGHSCORE_FALLBACK_KEY, GameServices.UNDEFINED_SCORE);
        onlineHighscore = highscoreFallback;

        gameServices.loadCurrentHighscore(Leaderboards.Keys.LEADERBOARD, new GameServices.LoadHighscoreCallback() {
            @Override
            public void success(long scoreResult) {
                onlineHighscore = Math.max(scoreResult, highscoreFallback);

                LOG.debug("Loaded users online highscore: " + onlineHighscore);
            }

            @Override
            public void error() {
                LOG.error("Failed to load user's online highscore");
            }
        });
    }

    private void refreshAchievements() {
        gameServices.loadAchievements(false, new GameServices.LoadAchievementsCallback() {
            @Override
            public void success(Map<String, Boolean> achievementsResult) {
                onlineAchievements = achievementsResult;
            }

            @Override
            public void error() {
                LOG.error("Failed to load online achievements");
            }
        });
    }

    public void checkAndUnlockAchievement(int currentCollectedFruits, int currentSnakeSize) {
        if (checkAchievementCanBeUnlocked(Achievements.Keys.POLOGNAISE_25, currentSnakeSize, 25)) {
            unlockAchievement(Achievements.Keys.POLOGNAISE_25);
        } else if (checkAchievementCanBeUnlocked(Achievements.Keys.POLOGNAISE_50, currentSnakeSize, 50)) {
            unlockAchievement(Achievements.Keys.POLOGNAISE_50);
        } else if (checkAchievementCanBeUnlocked(Achievements.Keys.FRUITS_10, currentCollectedFruits, 10)) {
            unlockAchievement(Achievements.Keys.FRUITS_10);
        } else if (checkAchievementCanBeUnlocked(Achievements.Keys.FRUITS_25, currentCollectedFruits, 25)) {
            unlockAchievement(Achievements.Keys.FRUITS_25);
        } else if (checkAchievementCanBeUnlocked(Achievements.Keys.FRUITS_100, currentCollectedFruits, 100)) {
            unlockAchievement(Achievements.Keys.FRUITS_100);
        }
    }

    private boolean checkAchievementCanBeUnlocked(String achievementKey, int currentValue, int targetValue) {
        return currentValue >= targetValue &&
                onlineAchievements.containsKey(achievementKey) &&
                !onlineAchievements.get(achievementKey);
    }

    private void unlockAchievement(String achievementKey) {
        onlineAchievements.put(achievementKey, true);
        gameServices.unlockAchievement(achievementKey);
    }

    public void submitScore(long score) {
        gameServices.submitScore(Leaderboards.Keys.LEADERBOARD, score);

        if (score > onlineHighscore) {
            onlineHighscore = score;

            prefs.putLong(HIGHSCORE_FALLBACK_KEY, score);
            prefs.flush();
        }
    }

    public long getOnlineHighscore() {
        return onlineHighscore;
    }

    @Override
    public boolean isSupported() {
        return gameServices.isSupported();
    }


    @Override
    public void signIn() {
        gameServices.signIn();
    }

    @Override
    public boolean isSignedIn() {
        return gameServices.isSignedIn();
    }

    @Override
    public void signOut() {
        gameServices.signOut();
    }

    @Override
    public void rateGame() {
        gameServices.rateGame();
    }

    @Override
    public void showAchievements() {
        gameServices.showAchievements();
    }

    @Override
    public void showScore(String leaderboardKey) {
        gameServices.showScore(leaderboardKey);
    }
}
