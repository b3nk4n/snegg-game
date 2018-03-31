package de.bsautermeister.snegg.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class AssetDescriptors {
    public static final AssetDescriptor<BitmapFont> UI_FONT =
            new AssetDescriptor<BitmapFont>(AssetPaths.UI_FONT, BitmapFont.class);

    public static final AssetDescriptor<TextureAtlas> GAME_PLAY_ATLAS =
            new AssetDescriptor<TextureAtlas>(AssetPaths.GAME_PLAY_ATLAS, TextureAtlas.class);

    public static final AssetDescriptor<Skin> UI_SKIN =
            new AssetDescriptor<Skin>(AssetPaths.UI_SKIN, Skin.class);

    private AssetDescriptors() {}
}
