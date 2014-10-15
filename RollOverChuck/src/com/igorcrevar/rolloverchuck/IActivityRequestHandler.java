package com.igorcrevar.rolloverchuck;

public interface IActivityRequestHandler {
	public static interface IConfirmDialogCallback {
		void confirmDialogResult(boolean result);
	}
	void rate();
	void showAd();
	void share();
	void updateLeaderboardAndAchievements();
	void showAchievements();
	void showLeaderboard(GameType gameType);
	boolean getSignedInGPGS();
	void loginGPGS();
	void setGameManager(GameManager gameManager);
	void confirmDialog(final String txt, final IConfirmDialogCallback callback);
}
