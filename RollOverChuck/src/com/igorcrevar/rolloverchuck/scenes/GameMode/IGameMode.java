package com.igorcrevar.rolloverchuck.scenes.GameMode;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface IGameMode {	
	public void init();
	public void update(float timer, float deltaTime);
	public void drawSprites(BitmapFont font, SpriteBatch spriteBatch);
	public TextureRegion getFieldTextureRegion();
	public void touchUp(int screenX, int screenY, int pointer, int button);
	public void postUpdate(SpriteBatch spriteBatch);
	public void dispose();
}
