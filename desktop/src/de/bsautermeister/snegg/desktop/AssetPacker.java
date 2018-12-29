package de.bsautermeister.snegg.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.nio.file.Paths;

public class AssetPacker {
    private static final String RAW_ASSETS_PATH = "desktop/assets-raw";
    private static final String ASSETS_PATH = "android/assets";

    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxHeight = 2048;

        pack(settings, "gameplay");
        pack(settings, "ui");
        pack(settings, "loading");
    }

    private static void pack(TexturePacker.Settings settings, String name) {
        TexturePacker.process(settings,
                Paths.get(RAW_ASSETS_PATH, name).toString(),
                Paths.get(ASSETS_PATH, name).toString(),
                name);
    }
}
