package de.bsautermeister.snegg.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class AssetDescriptors {
    public interface Fonts {
        AssetDescriptor<BitmapFont> SMALL =
                new AssetDescriptor<BitmapFont>(AssetPaths.Fonts.SMALL, BitmapFont.class);

        AssetDescriptor<BitmapFont> BIG =
                new AssetDescriptor<BitmapFont>(AssetPaths.Fonts.BIG, BitmapFont.class);
    }

    public interface Atlas {
        AssetDescriptor<TextureAtlas> LOADING =
                new AssetDescriptor<TextureAtlas>(AssetPaths.Atlas.LOADING, TextureAtlas.class);

        AssetDescriptor<TextureAtlas> GAMEPLAY =
                new AssetDescriptor<TextureAtlas>(AssetPaths.Atlas.GAMEPLAY, TextureAtlas.class);
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
        AssetDescriptor<Sound> FRUIT_SPAWN =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.FRUIT_SPAWN, Sound.class);
    }

    public static final AssetDescriptor[] ALL = {
      Fonts.SMALL, Fonts.BIG,
      Atlas.LOADING, Atlas. GAMEPLAY,
      Skins.UI,
      Sounds.COIN, Sounds.LOSE, Sounds.FRUIT, Sounds.FRUIT_SPAWN
    };

    private AssetDescriptors() {}
}
