package de.bsautermeister.snegg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import de.bsautermeister.snegg.assets.AssetPaths;
import de.bsautermeister.snegg.audio.MusicPlayer;
import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.common.GameServiceManager;
import de.bsautermeister.snegg.listeners.RateGameListener;
import de.bsautermeister.snegg.screen.loading.LoadingScreen;
import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class SneggGame extends GameApp {

	private final static String SAVE_DAT_FILENAME = "snegg.dat";

	private static MusicPlayer musicPlayer;

	private static GameServiceManager gameServiceManager;

	public SneggGame(IGameServiceClient gameServiceClient, RateGameListener rateGameListener) {
		super(gameServiceClient, rateGameListener);
	}

	@Override
	public void create() {
		super.create();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		musicPlayer = new MusicPlayer();
		musicPlayer.setup(AssetPaths.Music.BACKGROUND_AUDIO, GameConfig.MUSIC_VOLUME);

		gameServiceManager = new GameServiceManager(getGameServiceClient());

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
