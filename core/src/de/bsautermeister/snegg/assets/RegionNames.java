package de.bsautermeister.snegg.assets;

public class RegionNames {

    public static final String LOADING_LOGO = "title";
    public static final String LOADING_ANIMATION = "loading";
    public static final String LOADING_FRAME = "frame";
    public static final String LOADING_BAR_HIDDEN = "loading-hidden";
    public static final String LOADING_BACKGROUND = "screen-bg";
    public static final String LOADING_FRAME_BACKGROUND = "frame-bg";

    public static final String BACKGROUND = "background";
    public static final String BACKGROUND_OVERLAY = "bg-overlay";
    public static final String HEAD = "head";
    public static final String HEAD_KILLED = "head-killed";
    public static final String[] HEAD_HAPPY = {"head-happy1", "head-happy2", "head-happy3"};
    public static final String[][] NESTS = { // nest_idx = egg, egg_idx = face
            {"nest1_egg1", "nest1_egg2", "nest1_egg3", "nest1_egg4", "nest1_egg5"},
            {"nest2_egg1", "nest2_egg2", "nest2_egg3", "nest2_egg4", "nest2_egg5"},
            {"nest3_egg1", "nest3_egg2", "nest3_egg3", "nest3_egg4", "nest3_egg5"},
            {"nest4_egg1", "nest4_egg2", "nest4_egg3", "nest4_egg4", "nest4_egg5"},
            {"nest5_egg1", "nest5_egg2", "nest5_egg3", "nest5_egg4", "nest5_egg5"}
    };
    public static final String EGGS[][] = {
            {"egg1_face1", "egg1_face2", "egg1_face3", "egg1_face4", "egg1_face5"},
            {"egg2_face1", "egg2_face2", "egg2_face3", "egg2_face4", "egg2_face5"},
            {"egg3_face1", "egg3_face2", "egg3_face3", "egg3_face4", "egg3_face5"},
            {"egg4_face1", "egg4_face2", "egg4_face3", "egg4_face4", "egg4_face5"},
            {"egg5_face1", "egg5_face2", "egg5_face3", "egg5_face4", "egg5_face5"}
    };
    public static final String WORM = "worm";
    public static final String WORM_HOLE = "worm-hole";
    public static final String WORM_SCARED = "worm-scared";

    public static final String TITLE = "title";
    public static final String PAUSE = "pause";
    public static final String GAME_OVER = "gameover";

    private RegionNames() {}
}
