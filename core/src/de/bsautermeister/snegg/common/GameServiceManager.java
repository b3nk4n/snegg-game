package de.bsautermeister.snegg.common;

import com.badlogic.gdx.utils.Logger;

import java.util.HashMap;
import java.util.Map;

import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.services.Achievements;
import de.bsautermeister.snegg.services.GameServices;
import de.bsautermeister.snegg.services.Leaderboards;

public class GameServiceManager {
    private static final Logger LOG = new Logger(GameServiceManager.class.getSimpleName(), GameConfig.LOG_LEVEL);

    private static final String HIGHSCORE_KEY = "highscore";

    private final GameServices gameServices;

    // TODO do we really need to store both?
    private long onlineHighscore = GameServices.UNDEFINED_SCORE; //TODO save state for tombstoning?
    private Map<String, Boolean> onlineAchievements = new HashMap<String, Boolean>();

    public GameServiceManager(GameServices gameServices) {
        this.gameServices = gameServices;
    }

    public void refresh() {
        refreshOnline();
    }

    private void refreshOnline() {
        try {
            onlineHighscore = gameServices.loadCurrentHighscore(Leaderboards.Keys.LEADERBOARD);
            LOG.debug("GameServiceScore: " + onlineHighscore);

            onlineAchievements = gameServices.loadAchievements(false);
            LOG.debug("AchievementsMap: " + onlineAchievements.size());
        } catch (Exception ex) {
            // TODO: use local values as fallback? Is there actually an exception possible?
        }
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
        }
    }

    public long getOnlineHighscore() {
        return onlineHighscore;
    }
}
