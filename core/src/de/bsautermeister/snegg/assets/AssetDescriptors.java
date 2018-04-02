package de.bsautermeister.snegg.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class AssetDescriptors {
    public interface Fonts {
        AssetDescriptor<BitmapFont> UI =
                new AssetDescriptor<BitmapFont>(AssetPaths.Fonts.UI, BitmapFont.class);
    }

    public interface Atlas {
        AssetDescriptor<TextureAtlas> GAME_PLAY =
                new AssetDescriptor<TextureAtlas>(AssetPaths.Atlas.GAME_PLAY, TextureAtlas.class);
    }

    public interface Skins {
        AssetDescriptor<Skin> UI =
                new AssetDescriptor<Skin>(AssetPaths.Skins.UI, Skin.class);
    }

    public interface Sounds {
        AssetDescriptor<Sound> COIN =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.COIN, Sound.class);
        AssetDescriptor<Sound> LOSE =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.LOSE, Sound.class);
        AssetDescriptor<Sound> FRUIT =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.FRUIT, Sound.class);
    }

    private AssetDescriptors() {}
}
