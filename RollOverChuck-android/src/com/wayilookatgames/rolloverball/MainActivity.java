package com.wayilookatgames.rolloverball;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.igorcrevar.rolloverchuck.GameListener;
import com.igorcrevar.rolloverchuck.GameManager;
import com.igorcrevar.rolloverchuck.GameType;
import com.igorcrevar.rolloverchuck.GameType.BaseGameType;
import com.igorcrevar.rolloverchuck.IActivityRequestHandler;

public class MainActivity extends AndroidApplication implements IActivityRequestHandler, GameHelperListener {
	private static final boolean AreGPSEnabled = true;
	private static final boolean AreADSEnabled = true;
	private static final String GooglePlayUrl = "https://play.google.com/store/apps/details?id=com.wayilookatgames.rolloverball";
	private static final String AdUnitId = "your_add_id_here";
	
	private GameManager gameManager;
	private GameType gameType = new GameType();
	private InterstitialAd interstitial;
	private GameHelper mHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initialize(new GameListener(this));
		
		// Create and setup the AdMob view
	    if (AreADSEnabled) {
	    	// Create the interstitial.
	        interstitial = new InterstitialAd(this);
	        interstitial.setAdUnitId(AdUnitId);
	        // Create ad request.
	        AdRequest adRequest = new AdRequest.Builder().build();
	        // Begin loading your interstitial.
	        interstitial.loadAd(adRequest);
	    }
	   
	    // create google play service helper 
	    // do login automatically if user already signed into account 
	    if (AreGPSEnabled) {
	    	SharedPreferences sp = getSharedPreferences("GOOGLE_PLAY", Context.MODE_PRIVATE);
	    	boolean automaticSignIn = sp.getBoolean("signed_in", false);
	    	mHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
	    	mHelper.setConnectOnStart(automaticSignIn);
	    	mHelper.enableDebugLog(true);
	    	mHelper.setup(this);
	    }
	}

	@Override
	public void rate() {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(GooglePlayUrl));
		this.startActivity(i);
	}

	@Override
	public void share() {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "I am playing in Roll Over Ball!, great Android game!" + GooglePlayUrl);
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}

	@Override
	public void updateLeaderboardAndAchievements() {
		if (!AreGPSEnabled || !getSignedInGPGS() || gameManager == null) {
			return;
		}
		
		long score = gameManager.getTopScore(gameType.set(GameType.BaseGameType.Arcade, GameType.Difficulty.Easy));
		Games.Leaderboards.submitScore(mHelper.getApiClient(), getString(R.string.leaderboard_arcade_high_scores_easy), score);
		score = gameManager.getTopScore(gameType.set(GameType.BaseGameType.Arcade, GameType.Difficulty.Medium));
		Games.Leaderboards.submitScore(mHelper.getApiClient(), getString(R.string.leaderboard_arcade_high_scores_medium), score);
		score = gameManager.getTopScore(gameType.set(GameType.BaseGameType.Arcade, GameType.Difficulty.Hard));
		Games.Leaderboards.submitScore(mHelper.getApiClient(), getString(R.string.leaderboard_arcade_high_scores_die_hard), score);
		// for stress free we got just one leaderboard
		score = gameManager.getTopScore(gameType.set(GameType.BaseGameType.StressFree, GameType.Difficulty.Easy));
		long score2 = gameManager.getTopScore(gameType.set(GameType.BaseGameType.StressFree, GameType.Difficulty.Medium));
		long score3 = gameManager.getTopScore(gameType.set(GameType.BaseGameType.StressFree, GameType.Difficulty.Hard));
		Games.Leaderboards.submitScore(mHelper.getApiClient(), getString(R.string.leaderboard_stress_free), Math.max(score3, Math.max(score, score2)));		
	}

	@Override
	public void showAchievements() {
	}

	@Override
	public void showLeaderboard(GameType gameType) {
		if (!AreGPSEnabled) {
			return;
		}
		if (getSignedInGPGS()) {
			if (gameType.getBaseType() == GameType.BaseGameType.Arcade) {
				switch (gameType.getDifficulty()) {
				case Easy:
					startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mHelper.getApiClient(), getString(R.string.leaderboard_arcade_high_scores_easy)), 0x777);
					break;
				case Medium:
					startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mHelper.getApiClient(), getString(R.string.leaderboard_arcade_high_scores_medium)), 0x777);
					break;
				case Hard:
					startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mHelper.getApiClient(), getString(R.string.leaderboard_arcade_high_scores_die_hard)), 0x777);
					break;
				}
			}
			else if (gameType.getBaseType() == BaseGameType.StressFree) {
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mHelper.getApiClient(), getString(R.string.leaderboard_stress_free)), 0x777);
			}
		} 
		else {
			loginGPGS();
		}
	}

	@Override
	public boolean getSignedInGPGS() {
		return AreGPSEnabled && mHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		if (!AreGPSEnabled) {
			return;
		}
		try {
			runOnUiThread(new Runnable(){
				public void run() {
					mHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	@Override
	public void confirmDialog(final String txt, final IConfirmDialogCallback callback) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new AlertDialog.Builder(MainActivity.this)
				.setTitle("Confirm")
				.setMessage(txt)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								callback.confirmDialogResult(true);
								dialog.dismiss();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						callback.confirmDialogResult(false);
						dialog.dismiss();
					}
				}).create().show();				
			}
		});
	}

	@Override
	public void showAd() {
		if (AreADSEnabled) {
			long totalGames = gameManager.getAllGamesPlayed();
			long factor = gameManager.getGameData().AdShowOn;
			if ((totalGames % factor) == 0) {				
				runOnUiThread(new Runnable(){
					public void run() {
						interstitial.show();
					}
				});
			}
		}
	}
	
	@Override
    protected void onStart() {
        super.onStart();
        if (AreGPSEnabled) {
        	mHelper.onStart(this);
        }
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        if (AreGPSEnabled) {
        	mHelper.onStop();
        }
    }
    
    @Override
    protected void onActivityResult(int request, int response, Intent data) {
    	super.onActivityResult(request, response, data);
    	if (AreGPSEnabled && response == 0x777) {
            mHelper.onActivityResult(request, response, data);
    	}
    }

	@Override
	public void onSignInFailed() {	
	}

	@Override
	public void onSignInSucceeded() {
		// if signed in sucessifull mark that so next time sign in will be done automatically
		SharedPreferences sp = getSharedPreferences("GOOGLE_PLAY", Context.MODE_PRIVATE);
		sp.edit().putBoolean("signed_in", true).commit();
		// update achievements/etc if needed
		updateLeaderboardAndAchievements();
	}
}
