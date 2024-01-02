package de.bsautermeister.snegg.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

import java.util.HashMap;
import java.util.Map;

import de.bsautermeister.snegg.GameConfig;
import de.bsautermeister.snegg.services.Achievements;
import de.bsautermeister.snegg.services.Leaderboards;
import de.golfgl.gdxgamesvcs.GameServiceException;
import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.MockGameServiceClient;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;
import de.golfgl.gdxgamesvcs.achievement.IAchievement;
import de.golfgl.gdxgamesvcs.achievement.IFetchAchievementsResponseListener;
import de.golfgl.gdxgamesvcs.leaderboard.IFetchLeaderBoardEntriesResponseListener;
import de.golfgl.gdxgamesvcs.leaderboard.ILeaderBoardEntry;

public class GameServiceManager {
    private static final Logger LOG = new Logger(GameServiceManager.class.getSimpleName(), GameConfig.LOG_LEVEL);

    private static final String HIGHSCORE_FALLBACK_KEY = "highscore";
    private static final long UNDEFINED_SCORE = -1;

    private final IGameServiceClient gameServiceClient;

    private final Preferences prefs;
    private long onlineHighscore = UNDEFINED_SCORE;

    /**
     * Map of unlocked achievements by specific-key.
     */
    private final Map<String, Boolean> onlineAchievements = new HashMap<>();

    public GameServiceManager(IGameServiceClient gameServiceClient) {
        this.gameServiceClient = gameServiceClient;
        this.prefs = Gdx.app.getPreferences(GameServiceManager.class.getName());
    }

    public void refresh() {
        refreshUserHighscore();
        refreshAchievements();
    }

    private void refreshUserHighscore() {
        final long highscoreFallback = prefs.getLong(HIGHSCORE_FALLBACK_KEY, UNDEFINED_SCORE);
        onlineHighscore = highscoreFallback;

        gameServiceClient.fetchLeaderboardEntries(Leaderboards.Keys.LEADERBOARD, 1, true,
                new IFetchLeaderBoardEntriesResponseListener() {
                    @Override
                    public void onLeaderBoardResponse(Array<ILeaderBoardEntry> leaderBoard) {
                        if (!leaderBoard.isEmpty()) {
                            ILeaderBoardEntry entry = leaderBoard.get(0);
                            int scoreValue = (int) entry.getSortValue();
                            onlineHighscore = Math.max(scoreValue, highscoreFallback);
                        }
                    }
                });
    }

    private void refreshAchievements() {
        gameServiceClient.fetchAchievements(new IFetchAchievementsResponseListener() {
            @Override
            public void onFetchAchievementsResponse(Array<IAchievement> achievements) {
                for (String achievementKey : Achievements.ALL_KEYS) {
                    for (IAchievement achievement : achievements) {
                        // Check via IAchievement#isAchievementId is needed to respect the key mappings
                        if (achievement.isAchievementId(achievementKey)) {
                            onlineAchievements.put(achievementKey, achievement.isUnlocked());
                        }
                    }
                }
            }
        });
    }

    public void checkAndUnlockAchievement(int currentCollectedWorms, int currentSnakeSize) {
        if (checkAchievementCanBeUnlocked(Achievements.Keys.POLOGNAISE_25, currentSnakeSize, 25)) {
            unlockAchievement(Achievements.Keys.POLOGNAISE_25);
        } else if (checkAchievementCanBeUnlocked(Achievements.Keys.POLOGNAISE_50, currentSnakeSize, 50)) {
            unlockAchievement(Achievements.Keys.POLOGNAISE_50);
        } else if (checkAchievementCanBeUnlocked(Achievements.Keys.WORMS_10, currentCollectedWorms, 10)) {
            unlockAchievement(Achievements.Keys.WORMS_10);
        } else if (checkAchievementCanBeUnlocked(Achievements.Keys.WORMS_25, currentCollectedWorms, 25)) {
            unlockAchievement(Achievements.Keys.WORMS_25);
        } else if (checkAchievementCanBeUnlocked(Achievements.Keys.WORMS_100, currentCollectedWorms, 100)) {
            unlockAchievement(Achievements.Keys.WORMS_100);
        }
    }

    private boolean checkAchievementCanBeUnlocked(String achievementKey, int currentValue, int targetValue) {
        return currentValue >= targetValue &&
                onlineAchievements.containsKey(achievementKey) &&
                !onlineAchievements.get(achievementKey);
    }

    private void unlockAchievement(String achievementKey) {
        onlineAchievements.put(achievementKey, true);
        gameServiceClient.unlockAchievement(achievementKey);
    }

    public void submitScore(long score) {
        gameServiceClient.submitToLeaderboard(Leaderboards.Keys.LEADERBOARD, score, null);

        if (score > onlineHighscore) {
            onlineHighscore = score;

            prefs.putLong(HIGHSCORE_FALLBACK_KEY, score);
            prefs.flush();
        }
    }

    public long getOnlineHighscore() {
        return onlineHighscore;
    }

    public boolean hasOnlineAchievements () {
        return onlineAchievements.size() > 0;
    }

    public boolean isSupported() {
        return !(gameServiceClient instanceof MockGameServiceClient || gameServiceClient instanceof NoGameServiceClient);
    }

    public void showAchievements() {
        try {
            gameServiceClient.showAchievements();
        } catch (GameServiceException.NoSessionException nse) {
            LOG.info("Signing in");
            gameServiceClient.logIn();
        } catch (GameServiceException e) {
            LOG.error("Showing achievements failed", e);
        }
    }

    public void showScore(String leaderboardKey) {
        try {
            gameServiceClient.showLeaderboards(leaderboardKey);
        } catch (GameServiceException.NoSessionException nse) {
            LOG.info("Signing in");
            gameServiceClient.logIn();
        } catch (GameServiceException e) {
            LOG.error("Showing leaderboards failed", e);
        }
    }
}
