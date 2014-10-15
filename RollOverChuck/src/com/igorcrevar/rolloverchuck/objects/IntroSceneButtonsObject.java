package com.igorcrevar.rolloverchuck.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.igorcrevar.rolloverchuck.GameManager;
import com.igorcrevar.rolloverchuck.IActivityRequestHandler;
import com.igorcrevar.rolloverchuck.ISceneManager;

public class IntroSceneButtonsObject  {
	// buttons
	private GameButton[] buttons;
	private final GameButton soundButton;
	
	public IntroSceneButtonsObject(final ISceneManager sceneManager, final float initialY) {
		final GameManager gameManager = sceneManager.getGameManager();
		final IActivityRequestHandler activityRequestHandler = sceneManager.getRequestHandler();
		buttons = new GameButton[3];
		
		soundButton = addButton(new GameButton(getTextureForSoundButton(gameManager), 1700, initialY, 120, 120) {
			@Override
			protected void onClick() {
				gameManager.setSoundOn(!gameManager.getIsSoundOn());
				soundButton.changeTexture(getTextureForSoundButton(gameManager));
			}
		});
				
		addButton(new GameButton(getTextureRegion("rate", gameManager), 1700, initialY - 160f, 120, 120) {
			@Override
			protected void onClick() {
				activityRequestHandler.rate();
			}
		});	
		
		/*addButton(new GameButton(getTextureRegion("ach", gameManager), 300, upButtonsY, 200, 200) {
			@Override
			protected void onClick() {
				activityRequestHandler.showAchievements();
			}
		});
		*/
		addButton(new GameButton(getTextureRegion("share", gameManager), 1540, initialY - 160f, 120, 120) {
			@Override
			protected void onClick() {
				activityRequestHandler.share();
			}
		});		
	}
	
	private GameButton addButton(GameButton gb) {
		for (int i = 0; i < buttons.length; ++i) {
			if (buttons[i] == null) {
				buttons[i] = gb;
				break;
			}
		}
		
		return gb;
	}
	
	public boolean check(float x, float y) {		
		for (GameButton gb : buttons) {
			if (gb.check(x, y)) {
				return true;
			}
		}
		
		return false;
	}
		
	public void draw(SpriteBatch spriteBatch) {
		for (GameButton gb : buttons) {
			gb.draw(spriteBatch);
		}
	}
	
	private TextureRegion getTextureForSoundButton(GameManager gameManager) {
		if (gameManager.getIsSoundOn()) {
			return getTextureRegion("soundon", gameManager);
		}
		
		return getTextureRegion("soundoff", gameManager);
	}
	
	private TextureRegion getTextureRegion(String name, GameManager gameManager) {
		return gameManager.getTextureAtlas("widgets").findRegion(name);
	}
}
