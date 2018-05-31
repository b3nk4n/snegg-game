package de.bsautermeister.snegg.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

public class GooglePlayGameServices implements GameServices {
    private GameHelper gameHelper;
    private final static int requestCode = 1;

    private final Activity activity;

    private final String storeLink;

    public GooglePlayGameServices(Activity activity, String storeLink) {
        this.activity = activity;

        this.storeLink = storeLink;

        gameHelper = new GameHelper(activity, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(true);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
        {
            @Override
            public void onSignInFailed(){ }

            @Override
            public void onSignInSucceeded(){ }
        };

        gameHelper.setup(gameHelperListener);
    }

    public void start() {
        gameHelper.onStart(activity);
    }

    public void stop() {
        gameHelper.onStop();
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data)
    {
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public void signIn()
    {
        try
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void signOut()
    {
        try
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    gameHelper.signOut();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame()
    {
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(storeLink)));
    }

    @Override
    public void unlockAchievement(String key)
    {
        Games.Achievements.unlock(gameHelper.getApiClient(), key);
    }

    @Override
    public void submitScore(String leaderboardKey, int highScore)
    {
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(
                    gameHelper.getApiClient(), leaderboardKey, highScore);
        }
    }

    @Override
    public void showAchievements()
    {
        if (isSignedIn()) {
            activity.startActivityForResult(
                    Games.Achievements.getAchievementsIntent(
                            gameHelper.getApiClient()), requestCode);
        } else {
            signIn();
        }
    }

    @Override
    public void showScore(String leaderboardKey)
    {
        if (isSignedIn()) {
            activity.startActivityForResult(
                    Games.Leaderboards.getLeaderboardIntent(
                            gameHelper.getApiClient(),
                            leaderboardKey),
                    requestCode);
        } else {
            signIn();
        }
    }

    @Override
    public boolean isSignedIn()
    {
        return gameHelper.isSignedIn();
    }
}
