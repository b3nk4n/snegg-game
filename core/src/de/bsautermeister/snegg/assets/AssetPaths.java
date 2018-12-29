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
        String COIN = "sounds/coin.wav";
        String LOSE = "sounds/lose.wav";
        String FRUIT = "sounds/fruit.wav";
        String FRUIT_SPAWN = "sounds/spawn.wav";
    }

    public interface Music {
        String BACKGROUND_AUDIO = "music/ScottHolmesHappyUkulele.mp3";
    }

    private AssetPaths() {}
}
