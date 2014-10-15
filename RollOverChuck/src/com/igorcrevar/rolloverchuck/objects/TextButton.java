package com.igorcrevar.rolloverchuck.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.igorcrevar.rolloverchuck.utils.GameHelper;

public abstract class TextButton {
	private static Color defTxtColor = new Color(1f, 204f / 255f, 0, 1f);
	private float startX;
	private float startY;
	private float width;
	private float height;
	private String text;
	private float textScale;
	private boolean isEnabled;
	private TextureRegion txtRegion;
	
	public TextButton(BitmapFont font, String text, float y, float width, float height) {
		this(font, text, (1920f - width) / 2.0f, y, width, height, 1.0f);
	}
	
	public TextButton(BitmapFont font, String text, float x, float y, float width, float height) {
		this(font, text, x, y, width, height, 1.0f);
	}
	
	public TextButton(BitmapFont font, String text, float x, float y, float width, float height, float textScale) {
		this.text = text;
		this.startX = x;
		this.startY = y;
		this.width = width;
		this.height = height;
		this.textScale = textScale;
		isEnabled = true;
		txtRegion = new TextureRegion(font.getRegion().getTexture(), 310f / 512f, 470f / 512f, 1.0f, 1.0f);
	}
	
	public void draw(SpriteBatch spriteBatch, BitmapFont font) {
		spriteBatch.draw(txtRegion, startX, startY - height, width, height);
		font.setScale(textScale);		
		font.setColor(defTxtColor);
		TextBounds tb = font.getBounds(text);
		float tpX = (width - tb.width) / 2 + startX;
		float tpY = - (height - tb.height) / 2 + startY + 5.0f;
		font.draw(spriteBatch, text, tpX, tpY);
	}
	
	public boolean check(float x, float y) {
		if (isEnabled && GameHelper.tapPointInsideRectangle(x, y, startX, startY, width, height)) {
			onClick();
			return true;
		}
		
		return false;
	}
		
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	protected abstract void onClick();
}