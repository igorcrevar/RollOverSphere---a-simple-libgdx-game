package com.igorcrevar.rolloverchuck.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.igorcrevar.rolloverchuck.GameManager;
import com.igorcrevar.rolloverchuck.ISceneManager;
import com.igorcrevar.rolloverchuck.ISceneManager.Type;
import com.igorcrevar.rolloverchuck.utils.GameHelper;

public class EndGameButtonsObject {
	// buttons
	private GameButton[] buttons;
	
	public EndGameButtonsObject(final ISceneManager sceneManager, float buttonsY) {
		buttons = new GameButton[3];
		
		float size = 240f;
		float margin = (1920 - size * 3.0f - size) / 2.0f;
		addButton(new GameButton(getTextureRegion("intro", sceneManager.getGameManager()), margin, buttonsY, size, size) {
			@Override
			protected void onClick() {
				sceneManager.setScene(Type.IntroScene);
			}
		});
		
		addButton(new GameButton(getTextureRegion("highscore", sceneManager.getGameManager()), margin + size * 1.5f, buttonsY, size, size) {
			@Override
			protected void onClick() {
				sceneManager.getRequestHandler().showLeaderboard(sceneManager.getCurrentGameType());
			}
		});
		
		addButton(new GameButton(getTextureRegion("playagain", sceneManager.getGameManager()), margin + size * 3f, buttonsY, size, size) {
			@Override
			protected void onClick() {
				sceneManager.startGame();
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

	public void check(float x, float y) {
		x = GameHelper.screenX2OtherX(x, 1920);
		y = GameHelper.screenY2OtherY(y, 1080);
		for (GameButton gb : buttons) {
			if (gb.check(x, y)) {
				break;
			}
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		for (GameButton gb : buttons) {
			gb.draw(spriteBatch);
		}
	}

	private TextureRegion getTextureRegion(String name, GameManager gameManager) {
		TextureRegion tr = gameManager.getTextureAtlas("widgets").findRegion(name);
		return tr;
	}
}
