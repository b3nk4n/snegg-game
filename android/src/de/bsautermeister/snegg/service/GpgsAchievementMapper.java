package de.bsautermeister.snegg.service;

import de.bsautermeister.snegg.services.Achievements;
import de.golfgl.gdxgamesvcs.IGameServiceIdMapper;

public class GpgsAchievementMapper implements IGameServiceIdMapper<String> {
    @Override
    public String mapToGsId(String key) {
        if (Achievements.Keys.WORMS_10.equals(key)) {
            return "CgkIj43Q_dIBEAIQAA";
        }
        if (Achievements.Keys.WORMS_25.equals(key)) {
            return "CgkIj43Q_dIBEAIQAQ";
        }
        if (Achievements.Keys.WORMS_100.equals(key)) {
            return "CgkIj43Q_dIBEAIQAg";
        }
        if (Achievements.Keys.POLOGNAISE_25.equals(key)) {
            return "CgkIj43Q_dIBEAIQAw";
        }
        if (Achievements.Keys.POLOGNAISE_50.equals(key)) {
            return "CgkIj43Q_dIBEAIQBA";
        }

        return null;
    }
}
