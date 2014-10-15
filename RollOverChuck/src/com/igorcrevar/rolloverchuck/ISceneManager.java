package com.igorcrevar.rolloverchuck;

import com.igorcrevar.rolloverchuck.GameType.BaseGameType;
import com.igorcrevar.rolloverchuck.GameType.Difficulty;

public interface ISceneManager {
	public static enum GameState { Active, PAUSED, GAMEOVER, COUNTING };
	public static enum Type { GameLoadingScene, IntroScene, GameScene };
	
	GameManager getGameManager();
	void setScene(ISceneManager.Type sceneName);
	void startGame(); // start again last game
	void startGame(BaseGameType basGameType, Difficulty difficulty);
	IActivityRequestHandler getRequestHandler();	
	/**
	 * Finish current game
	 * @param save  score of current game if true
	 */
	void finishGame(boolean saveScore);
	/**
	 * Get current game type (or last)
	 * @return
	 */
	GameType getCurrentGameType();
	void addToScore(long score);
	long getCurrentScore();
	
	GameState getGameState();
	void setGameState(GameState gameState);
	
	boolean isGameActive();
	boolean isGamePaused();
	boolean isGameOver();
}
