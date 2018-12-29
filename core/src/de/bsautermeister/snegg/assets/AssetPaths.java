package de.bsautermeister.snegg.assets;

public final class AssetPaths {

    public interface Fonts {
        String SMALL = "ui/fonts/cartoon-24.fnt";
        String BIG = "ui/fonts/cartoon-48.fnt";
    }

    public interface Atlas {
        String LOADING = "loading/loading.atlas";
        String GAMEPLAY = "gameplay/gameplay.atlas";
    }

    public interface Skins {
        String UI = "ui/ui.json";
    }

    public interface Sounds {
        String COLLECT1 = "sounds/clucking0.wav";
        String COLLECT2 = "sounds/clucking1.wav";
        String COLLECT3 = "sounds/clucking2.wav";
        String COLLECT4 = "sounds/clucking3.wav";
        String LOSE = "sounds/lose.wav";
        String WORM_SPAWN1 = "sounds/spawn0.wav";
        String WORM_SPAWN2 = "sounds/spawn1.wav";
        String WORM_SPAWN3 = "sounds/spawn2.wav";
        String DIGGING1 = "sounds/digging0.wav";
        String DIGGING2 = "sounds/digging1.wav";
        String DIGGING3 = "sounds/digging2.wav";
    }

    public interface Music {
        String BACKGROUND_AUDIO = "music/ScottHolmesHappyUkulele.mp3";
    }

    private AssetPaths() {}
}
