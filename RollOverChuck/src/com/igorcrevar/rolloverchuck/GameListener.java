package com.igorcrevar.rolloverchuck;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.igorcrevar.rolloverchuck.GameType.BaseGameType;
import com.igorcrevar.rolloverchuck.GameType.Difficulty;
import com.igorcrevar.rolloverchuck.scenes.GameLoadingScene;
import com.igorcrevar.rolloverchuck.scenes.GameScene;
import com.igorcrevar.rolloverchuck.scenes.IntroScene;
import com.igorcrevar.rolloverchuck.scenes.GameMode.ArcadeGameMode;
import com.igorcrevar.rolloverchuck.scenes.GameMode.IGameMode;
import com.igorcrevar.rolloverchuck.scenes.GameMode.StressFreeGameMode;

public class GameListener implements ApplicationListener, ISceneManager {
	// keep to main scenes always in memory
	private IScene introScene;
	private GameScene gameScene;
	
	private GameManager gameManager;
	private IScene currentScene;
	private IActivityRequestHandler requestHandler;
	private ArcadeGameMode arcadeGameMode;
	private IGameMode stressFreeMode;
	private GameType currentGameType = new GameType();
	private long currentScore;
	private GameState currentGameState;
	
	public GameListener(IActivityRequestHandler requestHandler) {
		this.requestHandler = requestHandler;
	}
	
	@Override
	public void create() {
		this.gameManager = new GameManager();
		this.requestHandler.setGameManager(this.gameManager);
		this.setScene(ISceneManager.Type.GameLoadingScene);
		// input processor
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void dispose() {
		currentScene.leave(this);
		if (gameScene != null && gameScene != currentScene) {
			gameScene.dispose(this);
		}
		if (introScene != null && introScene != currentScene) {
			introScene.dispose(this);
		}
		if (currentScene != null) {
			currentScene.dispose(this);
		}
		gameManager.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		if (deltaTime > 0.041f) deltaTime = 0.041f; // make sure world does not update dramatically in one step
		currentScene.update(this, deltaTime);
	}

	@Override
	public void resize(int arg0, int arg1) {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void setScene(ISceneManager.Type scene) {
		if (currentScene != null) {
			currentScene.leave(this);
		}
		
		switch (scene) {
		case GameLoadingScene:
			currentScene = new GameLoadingScene(this);
			break;
		case IntroScene:
			if (introScene == null) {
				introScene = new IntroScene(this);
			}

			currentScene = introScene;
			break;
		case GameScene:
			if (gameScene == null) {
				gameScene = new GameScene(this);
			}
			
			currentScene = gameScene;
			break;
		}
		
		currentScene.init(this);
	}

	@Override
	public IActivityRequestHandler getRequestHandler() {
		return requestHandler;
	}
	
	@Override
	public GameManager getGameManager() {
		return gameManager;
	}

	@Override
	public void finishGame(boolean saveScore) {
		gameManager.stopGameMusic();
		if (saveScore) {
			gameManager.saveScore(currentGameType, currentScore);
		}
		requestHandler.updateLeaderboardAndAchievements();
		// if needed show ad!
		requestHandler.showAd();
	}

	@Override
	public GameType getCurrentGameType() {
		return currentGameType;
	}

	@Override
	public void addToScore(long score) {
		currentScore += score;
	}

	@Override
	public long getCurrentScore() {
		return currentScore;
	}

	@Override
	public void startGame() {
		// just reply last game
		startGame(currentGameType.getBaseType(), currentGameType.getDifficulty());
	}

	@Override
	public void startGame(BaseGameType basGameType, Difficulty difficulty) {		
		// reset score
		currentScore = 0;
		// update current game type
		currentGameType.set(basGameType, difficulty);
		// init game data properties
		getGameManager().getGameData().initForDifficulty(currentGameType);
		// game state is counting
		currentGameState = GameState.COUNTING;
		// set game scene as new scene
		setScene(Type.GameScene);
		// update additional things
		switch (basGameType) {
		case Arcade:
			if (arcadeGameMode == null) {
				arcadeGameMode = new ArcadeGameMode(this);
			}
			arcadeGameMode.init();
			gameScene.setGameMode(arcadeGameMode);
			break;
		case StressFree:
			if (stressFreeMode == null) {
				stressFreeMode = new StressFreeGameMode(this);
			}
			stressFreeMode.init();
			gameScene.setGameMode(stressFreeMode);
			break;
		}
	}

	@Override
	public GameState getGameState() {
		return currentGameState;
	}

	@Override
	public void setGameState(GameState gameState) {
		currentGameState = gameState;
	}

	@Override
	public boolean isGameActive() {
		return currentGameState == GameState.Active;
	}

	@Override
	public boolean isGamePaused() {
		return currentGameState == GameState.PAUSED;
	}

	@Override
	public boolean isGameOver() {
		return currentGameState == GameState.GAMEOVER;
	}
}
