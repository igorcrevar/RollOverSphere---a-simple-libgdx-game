package com.igorcrevar.rolloverchuck.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.igorcrevar.rolloverchuck.GameManager;
import com.igorcrevar.rolloverchuck.GameType.BaseGameType;
import com.igorcrevar.rolloverchuck.GameType.Difficulty;
import com.igorcrevar.rolloverchuck.IActivityRequestHandler.IConfirmDialogCallback;
import com.igorcrevar.rolloverchuck.IScene;
import com.igorcrevar.rolloverchuck.ISceneManager;
import com.igorcrevar.rolloverchuck.objects.IntroSceneButtonsObject;
import com.igorcrevar.rolloverchuck.objects.TextButton;
import com.igorcrevar.rolloverchuck.utils.GameHelper;
import com.igorcrevar.rolloverchuck.utils.Mathf;
import com.igorcrevar.rolloverchuck.utils.MyFontDrawer;
import com.igorcrevar.rolloverchuck.utils.MyFontDrawerBatch;
import com.igorcrevar.rolloverchuck.utils.MyFontDrawerDefaultFont;

public class IntroScene implements IScene, InputProcessor {
	private SpriteBatch spriteBatch = new SpriteBatch();
	private String[] title = new String[] { "R", "o", "l", "l", null, "o", "v", "e", "r", null, "B", "a", "l", "l", "!" };
	private MyFontDrawer[] titleDrawers = new MyFontDrawer[title.length];
	private float[] titleXPos = new float[title.length];
	private String creditsText = "Roll over Ball! by WayILook@Games (c) 2014 ..... " + 
								 "Concept & design, programming & blabla by Igor Crevar " +
								 "also unknown as \"random guy from somewhere over the nowhere\"";
	private float creditsPos;
	
	private GameManager gameManager;
	private float timer;
	
	private IntroSceneButtonsObject buttons;
	private ISceneManager sceneManager;
		
	// batch for custom font
	private MyFontDrawerBatch myFontDrawerBatch = new MyFontDrawerBatch(new MyFontDrawerDefaultFont(), 1920, 1080);
	
	private boolean isGameChoosen;
	private BaseGameType choosenBaseGameType;
	private TextButton[] gameTypeButtons;
	private TextButton[] difficultyTypeButtons;
	private float gameChoosenTimer;
	private TextureRegion textureRegionForTitle;
	
	public IntroScene(ISceneManager sceneManager) {
		this.sceneManager = sceneManager;
		this.gameManager = sceneManager.getGameManager();
		this.textureRegionForTitle = gameManager.getTextureAtlas("base").findRegion("myfont2");
		float titleX = 320f; // 220.0f;
		int i = 0;
		for (String letter : title) {
			titleXPos[i] = titleX;
			if (letter != null) {
				titleDrawers[i] = new MyFontDrawer(letter, 20f, 20f, 10.0f, 0.00001f, textureRegionForTitle, -60f, 80f, Float.NaN, 1.0f);
				myFontDrawerBatch.addNew(titleDrawers[i]);
				titleX += titleDrawers[i].getWidth() + 15.0f;				
			}
			else {
				titleDrawers[i] = null;
				titleX += 60.0f;
			}
			
			++i;
		}
		
		MyFontDrawer fnt2 = myFontDrawerBatch.addNew(new MyFontDrawer("(c) WayILook@Games 2014", 10f, 10f, 5.0f, 0.00001f, 
													 gameManager.getTextureAtlas("base").findRegion("myfont"), 0f, 0f, 1.0f, 1.0f)); 
		fnt2.translate((1920 - fnt2.getWidth()) / 2.0f, fnt2.getHeight() + 20, 0.0f);
		
		buttons = new IntroSceneButtonsObject(sceneManager, 600f);
		final IntroScene thisObj = this;
		gameTypeButtons = new TextButton[2];
		gameTypeButtons[0] = new TextButton(gameManager.getBitmapFont(), "Arcade", 600.0f, 600f, 120.0f) {			
			@Override
			protected void onClick() {
				thisObj.chooseGameType(BaseGameType.Arcade);
			}
		};
		gameTypeButtons[1] = new TextButton(gameManager.getBitmapFont(), "Stress Free", 400.0f, 600f, 120.0f) {			
			@Override
			protected void onClick() {
				thisObj.chooseGameType(BaseGameType.StressFree);
			}
		};
						
		difficultyTypeButtons = new TextButton[3];
		difficultyTypeButtons[0] = new TextButton(gameManager.getBitmapFont(), "Easy", 800.0f, 600f, 120f) {			
			@Override
			protected void onClick() {
				thisObj.chooseDifficulty(Difficulty.Easy);
			}
		};
		difficultyTypeButtons[1] = new TextButton(gameManager.getBitmapFont(), "Medium", 600.0f, 600f, 120f) {			
			@Override
			protected void onClick() {
				thisObj.chooseDifficulty(Difficulty.Medium);
			}
		};
		difficultyTypeButtons[2] = new TextButton(gameManager.getBitmapFont(), "Die hard!", 400.0f, 600f, 120f) {			
			@Override
			protected void onClick() {
				thisObj.chooseDifficulty(Difficulty.Hard);
			}
		};
	}
	
	public void chooseGameType(BaseGameType baseGameType) {
		isGameChoosen = true;
		gameChoosenTimer = 0.0f;
		this.choosenBaseGameType = baseGameType;
	}
	
	public void chooseDifficulty(Difficulty diff) {
		sceneManager.startGame(choosenBaseGameType, diff);
	}
	
	@Override
	public void init(ISceneManager sceneManager) {
		Gdx.input.setInputProcessor(this);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);		 
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
		
		gameManager.playIntroMusic();
		timer = 0.0f;
		creditsPos = 1920.0f;
		isGameChoosen = false;
		
		sceneManager.getGameManager().getStars().init(sceneManager.getGameManager().getTextureAtlas("base").findRegion("star"));
	}

	@Override
	public void update(ISceneManager sceneManager, float deltaTime) {
		GameHelper.clearScreen();
		
		GameHelper.setProjectionFor2D(spriteBatch, 1920, 1080);
		spriteBatch.begin();
		sceneManager.getGameManager().getStars().update(deltaTime);
		sceneManager.getGameManager().getStars().draw(spriteBatch);
		spriteBatch.end();
		
		BitmapFont font = gameManager.getBitmapFont();
		if (!isGameChoosen) {
			float angle = Mathf.lerp(359.0f, 0.0f, timer * 0.45f);
			float titleScale = Mathf.lerp(4.0f, 1.0f, timer * 0.5f);
			float ntimer = (timer - (int)Math.max(timer - 0, 0f)) / 1.0f; // every two seconds all colors in texture are rotated
			float npos = (int)Mathf.lerp(1f, 10f, ntimer) / 10.0f; 
			for (int i = 0; i < titleDrawers.length; ++i) {
				MyFontDrawer fd = titleDrawers[i];
				if (fd != null) {
					fd.setUVMinMaxScrollV(textureRegionForTitle, npos, 0.3f);
					fd.idt().translate(titleXPos[i], 800, 0.0f).scale(titleScale, titleScale, 0.0f).rotateAround(0.0f, 0.0f, 1.0f, angle);
				}
			}
			
			myFontDrawerBatch.draw(gameManager.getShader("region"), textureRegionForTitle.getTexture());
			
			GameHelper.setProjectionFor2D(spriteBatch, 1920, 1080);
			spriteBatch.begin();
			font.setScale(0.6f);
			font.setColor(Color.WHITE);
			font.draw(spriteBatch, creditsText, creditsPos, 180);
			creditsPos -= 200.0f * deltaTime;
			if (creditsPos < -font.getBounds(creditsText).width - 20.0f) {
				creditsPos = 1920;
			}
			
			buttons.draw(spriteBatch);
			// draw textButtons
			for (TextButton tb : gameTypeButtons) {
				tb.draw(spriteBatch, font);
			}
			
		}
		// if game choosen second menu show
		else {
			GameHelper.setProjectionFor2D(spriteBatch, 1920, 1080);
			spriteBatch.begin();
			float scale = Mathf.lerp(0.0f, 1.0f, gameChoosenTimer * 4f);
			Sprite overlaySprite = sceneManager.getGameManager().getOverlaySprite(1.0f, 0.95f);
			overlaySprite.setScale(scale);
			overlaySprite.draw(spriteBatch);
			if (scale == 1.0f) {
				for (TextButton tb : difficultyTypeButtons) {
					tb.draw(spriteBatch, font);
				}
			}
			
			gameChoosenTimer += deltaTime;
		}
		
		spriteBatch.end();
		
		timer += deltaTime;
	}

	@Override
	public void leave(ISceneManager sceneManager) {
		gameManager.stopIntroMusic();
	}

	@Override
	public void dispose(ISceneManager sceneManager) {
		spriteBatch.dispose();
		myFontDrawerBatch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			if (isGameChoosen) {
				isGameChoosen = false;
			}
			else {
				sceneManager.getRequestHandler().confirmDialog("Quit game?", new IConfirmDialogCallback() {			
					@Override
					public void confirmDialogResult(boolean result) {
						if (result) Gdx.app.exit();
					}
				});
			}
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		float x = GameHelper.screenX2OtherX(screenX, 1920);
		float y = GameHelper.screenY2OtherY(screenY, 1080);
		if (isGameChoosen) {
			for (TextButton tb : difficultyTypeButtons) {
				tb.check(x, y);
			}
		}
		else {
			buttons.check(x, y);
			for (TextButton tb : gameTypeButtons) {
				tb.check(x, y);
			}
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {		
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}	
}
