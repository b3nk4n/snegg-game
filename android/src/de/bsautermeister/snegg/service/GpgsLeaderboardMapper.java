package de.bsautermeister.snegg.service;

import de.bsautermeister.snegg.services.Leaderboards;
import de.golfgl.gdxgamesvcs.IGameServiceIdMapper;

public class GpgsLeaderboardMapper implements IGameServiceIdMapper<String> {
    @Override
    public String mapToGsId(String key) {
        if (Leaderboards.Keys.LEADERBOARD.equals(key)) {
            return "CgkIj43Q_dIBEAIQBQ";
        }

        return null;
    }
}
