package com.igorcrevar.rolloverchuck.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.igorcrevar.rolloverchuck.IScene;
import com.igorcrevar.rolloverchuck.ISceneManager;
import com.igorcrevar.rolloverchuck.ISceneManager.Type;
import com.igorcrevar.rolloverchuck.utils.GameHelper;

public class GameLoadingScene implements IScene {
	private SpriteBatch spriteBatch = new SpriteBatch();
	private boolean isDisposed;
	
	public GameLoadingScene(ISceneManager sceneManager) {
	}
	
	@Override
	public void init(ISceneManager sceneManager) {
	}

	@Override
	public void update(ISceneManager sceneManager, float deltaTime) {
		GameHelper.clearScreen();
		if (sceneManager.getGameManager().updateLoading()) {
			sceneManager.setScene(Type.IntroScene);
			return;
		}
		
		if (sceneManager.getGameManager().isBitmapFontLoaded()) {
			GameHelper.setProjectionFor2D(spriteBatch, 1920, 1080);
			spriteBatch.begin();
			// draw score
			BitmapFont font = sceneManager.getGameManager().getBitmapFont();
			font.setScale(1.0f);
			font.setColor(Color.WHITE);
			String txt = "loading...";
			font.draw(spriteBatch, txt, GameHelper.getAlignedPosX(font, txt, 1920), GameHelper.getAlignedPosY(font, txt, 1080) + 100);
			spriteBatch.end();
		}
	}

	@Override
	public void leave(ISceneManager sceneManager) {
		dispose(sceneManager);
	}

	@Override
	public void dispose(ISceneManager sceneManager) {
		if (!isDisposed) {
			spriteBatch.dispose();
			isDisposed = true;
		}
	}
}