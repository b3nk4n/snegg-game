package de.bsautermeister.snegg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import de.bsautermeister.snegg.common.GameApp;
import de.bsautermeister.snegg.screen.loading.LoadingScreen;
import de.bsautermeister.snegg.services.GameServices;

public class SneggGame extends GameApp {

	public SneggGame(GameServices gameServices) {
		super(gameServices);
	}

	@Override
	public void create() {
		super.create();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		setScreen(new LoadingScreen(this));
	}
}
