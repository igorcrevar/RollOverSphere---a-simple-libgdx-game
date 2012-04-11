package com.igorcrevar.rolloversphere.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class MyInputAdapter extends InputAdapter {
	private int mLastX, mLastY;
	private int mDownX, mDownY;
	private int mClickX, mClickY;
	private boolean mCanBeDoubleClick = false;
	private long mTimeOfClick = 0, mTimeOfDown = 0;
	private IMyInputAdapter mMyAdapter;
	
	public MyInputAdapter(IMyInputAdapter myAdapter)
	{
		super();
		mMyAdapter = myAdapter;
	}
	
	@Override
    public boolean touchDragged (int x, int y, int pointer) {
		int dx = x - mLastX;
		int dy = y - mLastY;
		mLastX = x;
		mLastY = y;
		
		mMyAdapter.onMove(dx, dy, x, y);
		return true;
    }

	private boolean isNear(float x1, float y1, float x2, float y2){
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) < 10.0;
	}
	
	@Override
    public boolean touchUp(int x, int y, int pointer, int button) {
		long time = System.currentTimeMillis();
		
		//down+up happened in little amount of time
		if (time - mTimeOfDown <= 400){			
			//pressed and released coords are almost equal
			if (isNear(mDownX, mDownY, x, y)){
				//was click before not long time before down and coords between first click and this click are almost equal
				if (mCanBeDoubleClick && isNear(mClickX, mClickY, mDownX, mDownY)){
					mTimeOfClick = 0; 
					mMyAdapter.onDoubleClick(mDownX, mDownY);
				}
				else{
					mTimeOfClick = time;
					mClickX = mDownX;
					mClickY = mDownY;
					mMyAdapter.onClick(mDownX, mDownY);
				}
			}
		}
		
		mCanBeDoubleClick = false;
		mTimeOfDown = 0;
		return true;
    }
	
	@Override
    public boolean touchDown(int x, int y, int pointer, int button) {
		mDownX = mLastX = x;
		mDownY = mLastY = y;
		
		mTimeOfDown = System.currentTimeMillis();
		if (mTimeOfClick > 0  &&  mTimeOfDown -  mTimeOfClick < 400){
			mCanBeDoubleClick = true;
		}
		else{
			mCanBeDoubleClick = false;
			mTimeOfClick = 0;
		}
		
		return true;
    }

	 @Override
	 public boolean keyDown(int keycode) {
	    if(keycode == Keys.BACK || keycode == Keys.ESCAPE){
	    	mMyAdapter.onBack();
	    }
	    return false;
	 }
}
