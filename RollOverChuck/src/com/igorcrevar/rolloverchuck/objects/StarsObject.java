package com.igorcrevar.rolloverchuck.objects;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StarsObject {
	private class Star {
		public float speed;
		public Sprite sprite;
		public Star() {
			sprite = new Sprite();
		}
		
		public void setTexture(TextureRegion tr) {
			sprite.setRegion(tr);
		}
		
		public void init(float x, float y, float scale, float speed, float colorFactor) {
			sprite.setBounds(x, y, 32 * scale, 32 * scale);
			sprite.setColor(Color.WHITE.toFloatBits() * colorFactor);
			this.speed = speed;
		}
		
		public boolean update(float deltaTime) {
			sprite.setY(sprite.getY() - speed * deltaTime);
			return sprite.getY() >= 0;
		}
	}
	private static final int MAX_COUNT = 50;
	private Star[] points = new Star[MAX_COUNT];
	private Random rnd = new Random();
	
	public StarsObject() {
		for (int i = 0; i < MAX_COUNT; ++i) {
			points[i] = new Star();
		}
	}
	
	public void init(TextureRegion tr) {
		for (int i = 0; i < MAX_COUNT; ++i) {
			points[i].setTexture(tr);
			initStar(i, 1080);
		}
	}
	
	public void update(float deltaTime) {
		for (int i = MAX_COUNT - 1; i >= 0; --i) {
			Star p = points[i];					
			if (!p.update(deltaTime)) {
				initStar(i, 0); // create new on ith place
			}
		}
	}
	
	public void draw(SpriteBatch spriteBatch) {
		for (int i = MAX_COUNT - 1; i >= 0; --i) {
			Star p = points[i];
			p.sprite.draw(spriteBatch);
		}
	}
	
	private void initStar(int i, int randomY) {
		float yPos = 1080;
		if (randomY > 0) {
			yPos -= rnd.nextInt(randomY);
		}
		points[i].init(rnd.nextInt(1920), yPos, -rnd.nextFloat() + 1.0f, 75 + rnd.nextInt(150), rnd.nextFloat() * 0.3f + 0.7f);
	}	
}
