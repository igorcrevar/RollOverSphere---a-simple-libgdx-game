package com.igorcrevar.rolloversphere.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class PlayerPoint {
	public float x;
	public float y;
	public int points;
	public Color color;
	
	public PlayerPoint(float x, float y, int points) {
		super();
		this.x = x;
		this.y = y;
		this.points = points;
		this.color = new Color(0.9f, 0.8f, 0.9f, 1.0f);
	}	
	
	/**
	 * Updates point
	 * @param timeDiff
	 * @return true if point is still active after update
	 */
	public boolean update(float timeDiff){		
		y += Gdx.graphics.getHeight() / 5 * timeDiff;
		color.a -= timeDiff * 0.30f;
		
		return color.a > 0.0f  && y < Gdx.graphics.getHeight();
	}
}
