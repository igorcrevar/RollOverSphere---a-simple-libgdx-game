package com.igorcrevar.rolloverchuck.points;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PointsManager {
	private static final int MAX_COUNT = 10;
	private PlayerPoint[] points = new PlayerPoint[MAX_COUNT];
	private int numberActive;
	
	public PointsManager() {
		for (int i = 0; i < MAX_COUNT; ++i) {
			points[i] = new PlayerPoint();
		}
	}
	
	public void init() {
		numberActive = 0;
	}
	
	public void update(float deltaTime) {
		int start = numberActive - 1;
		for (int i = start; i >= 0; --i) {
			PlayerPoint p = points[i];					
			if (!p.update(deltaTime)) {
				// decrement number of active
				--numberActive;
				// put last active on i-th position
				PlayerPoint tmp = points[i];
				points[i] = points[numberActive];
				points[numberActive] = tmp;
			}
		}
	}
	
	public void draw(SpriteBatch spriteBatch, BitmapFont font) {
		for (int i = numberActive - 1; i >= 0; --i) {
			PlayerPoint p = points[i];
			font.setColor(p.color);
			font.draw(spriteBatch, p.points, p.x, p.y);
		}
	}
	
	public void add(int value, float x, float y) {
		PlayerPoint newPP = null;
		if (numberActive == MAX_COUNT) {
			// populate one with smallest alpha
			float val = 100.f;
			for (int i = 0; i < MAX_COUNT; ++i) {
				if (points[i].color.a < val) {
					newPP = points[i];
					val = newPP.color.a;					
				}
			}
			
			if (newPP == null) {
				Gdx.app.log("chuck", "PointsManager couldn't find a element in poll"); 
				// throw new NullPointerException("This should not happen!");
				return;
			}
		}
		else {
			newPP = points[numberActive++];
		}
		
		newPP.init(x, y, value);
	}	
}
