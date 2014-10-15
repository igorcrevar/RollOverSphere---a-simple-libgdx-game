package com.igorcrevar.rolloverchuck.scenes.GameMode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.igorcrevar.rolloverchuck.ISceneManager;
import com.igorcrevar.rolloverchuck.objects.EndGameButtonsObject;
import com.igorcrevar.rolloverchuck.utils.GameHelper;
import com.igorcrevar.rolloverchuck.utils.Mathf;
import com.igorcrevar.rolloverchuck.utils.MyFontDrawer;
import com.igorcrevar.rolloverchuck.utils.MyFontDrawerBatch;
import com.igorcrevar.rolloverchuck.utils.MyFontDrawerDefaultFont;

public class ArcadeGameMode implements IGameMode {
	private static final float PauseAfterEndGame = 2.0f;
	private int countDown;
	private ISceneManager sceneManager;
	private Sprite pointImage = new Sprite();
	private EndGameButtonsObject buttons;
	private float endGameTimer;
	private boolean isSaved;
	
	// batch for custom font
	private MyFontDrawerBatch myFontDrawerBatch = new MyFontDrawerBatch(new MyFontDrawerDefaultFont(), 1920, 1080);
	private MyFontDrawer title;
	
	public ArcadeGameMode(ISceneManager sceneManager) {
		this.sceneManager = sceneManager;
		
		buttons = new EndGameButtonsObject(sceneManager, 450);
		
		title = new MyFontDrawer("Time's up!", 25.0f, 25.0f, 10.0f, 0.00001f);
		title.setUVMinMax(sceneManager.getGameManager().getTextureAtlas("base").findRegion("myfont"));
		title = myFontDrawerBatch.addNew(title);
	}

	@Override
	public void init() {
		isSaved = false;
		countDown = getCountdownFrom();
		endGameTimer = 0.0f;
		
		pointImage.setRegion(sceneManager.getGameManager().getTextureAtlas("base").findRegion("clock"));
		pointImage.setSize(60, 60);
		pointImage.setPosition(1620, 850);
	}

	@Override
	public void update(float timer, float deltaTime) {
		if (sceneManager.isGameActive()) {
			countDown = getCountdownFrom() - (int)timer;
			if (countDown < 1) {
				sceneManager.setGameState(com.igorcrevar.rolloverchuck.ISceneManager.GameState.GAMEOVER);
				endGameTimer = 0.0f;
			}
		}
		else if (sceneManager.isGameOver()) {
			endGameTimer += deltaTime;
		}
	}

	@Override
	public void drawSprites(BitmapFont font, SpriteBatch spriteBatch) {
		pointImage.draw(spriteBatch);
		font.draw(spriteBatch, String.valueOf(countDown), 1700, 900);
		if (!sceneManager.isGameActive()) {
			float scale = Mathf.lerp(0.0f, 1.0f, endGameTimer / PauseAfterEndGame);
			Sprite overlaySprite = sceneManager.getGameManager().getOverlaySprite(0.0f, 0.65f);
			overlaySprite.setScale(scale);
			overlaySprite.draw(spriteBatch);
			
			if (endGameTimer > PauseAfterEndGame) {
				if (!isSaved) {
					isSaved = true;
					// save
					sceneManager.finishGame(true);
				}
				
				font.setScale(1.0f);
				String sc = "New Score: " + GameHelper.getStringPoints(sceneManager.getCurrentScore());
				font.setColor(Color.BLACK);
				font.draw(spriteBatch, sc, (1920 - font.getBounds(sc).width) / 2.0f + 5.0f, 650);
				font.setColor(Color.WHITE);
				font.draw(spriteBatch, sc, (1920 - font.getBounds(sc).width) / 2.0f, 660.0f);
				sc = "Top Score: " + GameHelper.getStringPoints(sceneManager.getGameManager().getTopScore(sceneManager.getCurrentGameType()));
				font.setColor(Color.BLACK);
				font.draw(spriteBatch, sc, (1920 - font.getBounds(sc).width) / 2.0f + 5.0f, 550);
				font.setColor(Color.WHITE);
				font.draw(spriteBatch, sc, (1920 - font.getBounds(sc).width) / 2.0f, 560.0f);
				
				buttons.draw(spriteBatch);
			}
		}
	}

	@Override
	public TextureRegion getFieldTextureRegion() {
		String texture = "";
		switch (sceneManager.getCurrentGameType().getDifficulty()) {
		case Easy:
			texture = "field1";
			break;
		case Medium:
			texture = "field2";
			break;
		case Hard:
			texture = "field3";
			break;
		}
		return sceneManager.getGameManager().getTextureAtlas("base").findRegion(texture);
	}

	@Override
	public void touchUp(int screenX, int screenY, int pointer, int button) {
		if (endGameTimer > PauseAfterEndGame) {
			buttons.check(screenX, screenY);
		}
	}
	
	private int getCountdownFrom() {
		return sceneManager.getGameManager().getGameData().ArcadeGameTimer;
	}

	@Override
	public void postUpdate(SpriteBatch spriteBatch) {
		if (!sceneManager.isGameActive()) {
			float xPos = Mathf.lerp(1920.0f, (1920 - title.getWidth()) / 2.0f, endGameTimer / PauseAfterEndGame);
			title.idt().translate(xPos, 900f, 0.0f);
			myFontDrawerBatch.draw(sceneManager.getGameManager().getShader("region"), sceneManager.getGameManager().getTextureAtlas("base").findRegion("myfont").getTexture());
		}
	}

	@Override
	public void dispose() {
		myFontDrawerBatch.dispose();
	}
}
