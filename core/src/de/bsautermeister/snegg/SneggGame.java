package de.bsautermeister.snegg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;

import de.bsautermeister.snegg.audio.MusicPlayer;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.config.GameConfig;
import de.bsautermeister.snegg.screen.loading.LoadingScreen;
import de.bsautermeister.snegg.services.GameServices;

public class SneggGame extends GameApp {
	private static final Logger LOGGER = new Logger(SneggGame.class.getName(), GameConfig.LOG_LEVEL);

	private final static String SAVE_DAT_FILENAME = "snegg.dat";

	private MusicPlayer musicPlayer = MusicPlayer.getInstance();

	public SneggGame(GameServices gameServices) {
		super(gameServices);
	}

	@Override
	public void create() {
		super.create();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		musicPlayer.setup("sounds/game_sound.mp3", GameConfig.MUSIC_VOLUME);

		setScreen(new LoadingScreen(this));
	}


	@Override
	public void render() {
		super.render();

		float delta = Gdx.graphics.getDeltaTime();
		musicPlayer.update(delta);
	}

	@Override
	public void resume() {
		super.resume();
		LOGGER.debug("RESUME");

		musicPlayer.play();
	}

	@Override
	public void pause() {
		super.pause();
		LOGGER.debug("PAUSE");

		musicPlayer.pause();
	}

	@Override
	public void dispose() {
		super.dispose();

		musicPlayer.dispose();
	}

	public static FileHandle getSavedDataHandle() {
		return Gdx.files.local(SAVE_DAT_FILENAME); // TODO: use internal, as soon as we are ready?
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
