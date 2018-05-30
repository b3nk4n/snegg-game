package de.bsautermeister.snegg.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.bsautermeister.snegg.SneggGame;
import de.bsautermeister.snegg.config.GameConfig;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int)GameConfig.WIDTH;
		config.height = (int)GameConfig.HEIGHT;

		new LwjglApplication(new SneggGame(null), config);
	}
}
