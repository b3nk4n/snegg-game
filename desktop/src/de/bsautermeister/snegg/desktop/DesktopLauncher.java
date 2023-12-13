package de.bsautermeister.snegg.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import de.bsautermeister.snegg.SneggGame;
import de.bsautermeister.snegg.GameConfig;
import de.bsautermeister.snegg.services.DummyGameServices;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Snegg");
		config.setWindowedMode((int)GameConfig.WIDTH, (int)GameConfig.HEIGHT);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new SneggGame(new DummyGameServices()), config);
	}
}
