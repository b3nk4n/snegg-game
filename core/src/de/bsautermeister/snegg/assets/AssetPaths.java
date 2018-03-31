package de.bsautermeister.snegg.assets;

public final class AssetPaths {

    public interface Fonts {
        String UI = "ui/fonts/oswald-32.fnt";
    }

    public interface Atlas {
        String GAME_PLAY = "gameplay/gameplay.atlas";
    }

    public interface Skins {
        String UI = "ui/ui.json";
    }

    public interface Sounds {
        String COIN = "sounds/coin.wav";
        String LOSE = "sounds/lose.wav";
    }

    private AssetPaths() {}
}
