package com.igorcrevar.rolloverchuck.points;

import com.badlogic.gdx.graphics.Color;

public class PlayerPoint {
	public float x;
	public float y;
	public String points;
	public Color color;
	
	public PlayerPoint() {
		super();
		this.color = new Color();
	}	
	
	public void init(float x, float y, int points) {
		this.x = x;
		this.y = y;
		this.points = Integer.toString(points);
		if (points > 0) {
			this.points = "+" + this.points;
		}
		this.color.set(0.9f, 0.8f, 0.9f, 1.0f);
	}
	
	/**
	 * Updates point
	 * @param deltaTime
	 * @return true if point is still active after update
	 */
	public boolean update(float deltaTime){		
		// 120 of virtual 1080
		y += 120.0f * deltaTime;
		color.a -= deltaTime * 0.50f;
		
		return color.a > 0.0f && y >= 0 && y <= 1920;
	}
}
