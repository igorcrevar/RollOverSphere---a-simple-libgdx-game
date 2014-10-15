package com.igorcrevar.rolloverchuck.scenes.GameMode;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.igorcrevar.rolloverchuck.ISceneManager;

public class StressFreeGameMode implements IGameMode {

	private ISceneManager sceneManager;

	public StressFreeGameMode(ISceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}
	
	@Override
	public void init() {
	}

	@Override
	public void update(float timer, float deltaTime) {
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
	public void drawSprites(BitmapFont font, SpriteBatch spriteBatch) {
	}

	@Override
	public void touchUp(int screenX, int screenY, int pointer, int button) {
	}

	@Override
	public void postUpdate(SpriteBatch spriteBatch) {
	}

	@Override
	public void dispose() {
	}
}
