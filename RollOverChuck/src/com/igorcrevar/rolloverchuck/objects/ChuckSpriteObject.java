package com.igorcrevar.rolloverchuck.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.igorcrevar.rolloverchuck.GameManager;
import com.igorcrevar.rolloverchuck.ISceneManager.GameState;
import com.igorcrevar.rolloverchuck.utils.Mathf;

public class ChuckSpriteObject {
	private Sprite main = new Sprite();
	private Sprite hand = new Sprite();
	private Sprite mouth = new Sprite();
	private float speed;
	private long prevScore;
	
	public ChuckSpriteObject(GameManager gameManager) {
		main.setSize(104, 104);
		hand.setSize(40, 39);
		mouth.setSize(33, 16);
		main.setRegion(gameManager.getTextureAtlas("base").findRegion("chuck_main"));
		hand.setRegion(gameManager.getTextureAtlas("base").findRegion("chuck_hand"));
		mouth.setRegion(gameManager.getTextureAtlas("base").findRegion("chuck_mouth"));
	}
	
	public void init() {
		main.setPosition(900, 950);
		hand.setPosition(890, 965);
		mouth.setPosition(935, 962);
		speed = 1.0f;
		prevScore = 0;
	}
	
	public void update(float timer, float deltaTime, long score, GameState gameState) {
		if (score > prevScore) {
			speed = Math.min(speed + (score - prevScore) * 0.15f, 3.0f);
		}
		else {
			speed = Math.max(1.0f, speed - deltaTime * 2.0f);
		}
		
		float tmp = timer / 4.0f * speed;
		tmp = tmp - (int)tmp;
		float posHand = Mathf.lerpBI(965f, 985f, tmp);
		hand.setPosition(890, posHand);
		float mouthHeight = Mathf.lerpBI(16f, 20f, tmp);
		mouth.setSize(33, mouthHeight);
		prevScore = score;
	}
	
	public void draw(SpriteBatch batch) {
		main.draw(batch);
		hand.draw(batch);
		mouth.draw(batch);
	}
}

