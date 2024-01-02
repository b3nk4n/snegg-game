package de.bsautermeister.snegg.services;

public class Achievements {
    public interface Keys {
        String WORMS_10 = "WORMS_10";
        String WORMS_25 = "WORMS_25";
        String WORMS_100 = "WORMS_100";
        String POLOGNAISE_25 = "POLOGNAISE_25";
        String POLOGNAISE_50 = "POLOGNAISE_50";
    }

    public static final String[] ALL_KEYS = {
            Keys.WORMS_10, Keys.WORMS_25, Keys.WORMS_100, Keys.POLOGNAISE_25, Keys.POLOGNAISE_50
    };
}
