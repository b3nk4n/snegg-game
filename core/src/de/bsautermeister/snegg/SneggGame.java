package de.bsautermeister.snegg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import de.bsautermeister.snegg.audio.MusicPlayer;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.common.GameServiceManager;
import de.bsautermeister.snegg.screen.loading.LoadingScreen;
import de.bsautermeister.snegg.services.GameServices;

public class SneggGame extends GameApp {

	private final static String SAVE_DAT_FILENAME = "snegg.dat";

	private static MusicPlayer musicPlayer;

	private final GameServices gameServices;
	private static GameServiceManager gameServiceManager;

	public SneggGame(GameServices gameServices) {
		this.gameServices = gameServices;
	}

	@Override
	public void create() {
		super.create();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		musicPlayer = new MusicPlayer();
		musicPlayer.setup("sounds/game_sound.mp3", GameConfig.MUSIC_VOLUME);

		gameServiceManager = new GameServiceManager(gameServices);

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
		musicPlayer.play();
	}

	@Override
	public void pause() {
		super.pause();
		musicPlayer.pause();
	}

	@Override
	public void dispose() {
		super.dispose();
		musicPlayer.dispose();
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

	public static GameServiceManager getGameServiceManager() {
		return gameServiceManager;
	}

	public static MusicPlayer getMusicPlayer() {
		return musicPlayer;
	}
}
