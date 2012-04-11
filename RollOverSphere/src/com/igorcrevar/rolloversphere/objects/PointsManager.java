package com.igorcrevar.rolloversphere.objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PointsManager {
	private List<PlayerPoint> mPoints = new ArrayList<PlayerPoint>();
	private BitmapFont mFont;
	private SpriteBatch mSpriteBatch;
	private int mPlayerScore;
	
	public PointsManager(BitmapFont font, SpriteBatch spriteBatch){
		mFont = font;
		mSpriteBatch = spriteBatch;
		mPlayerScore = 0;
	}
	
	public void update(float timeDiff){
		int size = mPoints.size();
		for (int i = size  - 1; i >= 0; --i){
			PlayerPoint p = mPoints.get(i);
			if (p.update(timeDiff)){
				mFont.setColor(p.color);
				mFont.draw(mSpriteBatch, Integer.toString(p.points), p.x, p.y);
			}
			else{
				mPoints.remove(i);		
			}
		}
	}
	
	public void add(int points, float x, float y){
		mPoints.add(new PlayerPoint(x, y, points));
		mPlayerScore += points;
	}
	
	public int getScore(){
		return mPlayerScore;
	}
}
