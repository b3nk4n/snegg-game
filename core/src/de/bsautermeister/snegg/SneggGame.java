package de.bsautermeister.snegg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;

import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.screen.ScreenBase;
import de.bsautermeister.snegg.screen.loading.LoadingScreen;
import de.bsautermeister.snegg.services.GameServices;

public class SneggGame extends GameApp {
	private static final Logger LOGGER = new Logger(SneggGame.class.getName(), GameConfig.LOG_LEVEL);

	private final static String SAVE_DAT_FILENAME = "snegg.dat";

	public SneggGame(GameServices gameServices) {
		super(gameServices);
	}

	@Override
	public void create() {
		super.create();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		setScreen(new LoadingScreen(this));
	}

	@Override
	public void pause() {
		super.pause();

		LOGGER.debug("PAUSE");
	}

	@Override
	public void resume() {
		super.resume();

		LOGGER.debug("RESUME");
	}

	public static FileHandle getSavedDataHandle() {
		return Gdx.files.local(SAVE_DAT_FILENAME);
	}

	public static void deleteSavedData() {
		final FileHandle handle = getSavedDataHandle();
		if (handle.exists())
			handle.delete();
	}

	public static boolean hasSavedData() {
		return getSavedDataHandle().exists();
	}
}
