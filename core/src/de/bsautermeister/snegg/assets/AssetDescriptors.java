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
        AssetDescriptor<Sound> COLLECT1 =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.COLLECT1, Sound.class);
        AssetDescriptor<Sound> COLLECT2 =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.COLLECT2, Sound.class);
        AssetDescriptor<Sound> COLLECT3 =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.COLLECT3, Sound.class);
        AssetDescriptor<Sound> COLLECT4 =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.COLLECT4, Sound.class);
        AssetDescriptor<Sound> LOSE =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.LOSE, Sound.class);
        AssetDescriptor<Sound> WORM_SPAWN1 =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.WORM_SPAWN1, Sound.class);
        AssetDescriptor<Sound> WORM_SPAWN2 =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.WORM_SPAWN2, Sound.class);
        AssetDescriptor<Sound> WORM_SPAWN3 =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.WORM_SPAWN3, Sound.class);
        AssetDescriptor<Sound> DIGGING1 =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.DIGGING1, Sound.class);
        AssetDescriptor<Sound> DIGGING2 =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.DIGGING2, Sound.class);
        AssetDescriptor<Sound> DIGGING3 =
                new AssetDescriptor<Sound>(AssetPaths.Sounds.DIGGING3, Sound.class);
    }

    public static final AssetDescriptor[] ALL = {
      Fonts.SMALL, Fonts.BIG,
      Atlas.LOADING, Atlas. GAMEPLAY,
      Skins.UI,
      Sounds.COLLECT1, Sounds.COLLECT2, Sounds.COLLECT3, Sounds.COLLECT4,
      Sounds.LOSE, Sounds.WORM_SPAWN1, Sounds.WORM_SPAWN2, Sounds.WORM_SPAWN3,
      Sounds.DIGGING1, Sounds.DIGGING2, Sounds.DIGGING3
    };

    private AssetDescriptors() {}
}
