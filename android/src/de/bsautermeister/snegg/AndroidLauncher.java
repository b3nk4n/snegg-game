package de.bsautermeister.snegg;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.bsautermeister.snegg.listeners.RateGameListener;
import de.golfgl.gdxgamesvcs.GpgsClient;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;

		GpgsClient gpgsClient = new GpgsClient()
				//.setGpgsLeaderboardIdMapper(new GpgsLeaderboardMapper())
				//.setGpgsAchievementIdMapper(new GpgsAchievementMapper())
				.initialize(this, false);

		initialize(new SneggGame(gpgsClient, this::rateInPlayStore), config);
	}

	private void rateInPlayStore() {
		Uri uri = Uri.parse("market://details?id=" + getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		// To count with Play market back-stack, in order to get back to our application
		goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
				Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
				Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
		}
	}
}
