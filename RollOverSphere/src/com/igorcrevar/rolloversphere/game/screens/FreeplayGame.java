package com.igorcrevar.rolloversphere.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.igorcrevar.rolloversphere.game.GameTypes;
import com.igorcrevar.rolloversphere.objects.boxes.factory.FreeplayBoxesFactory;
import com.igorcrevar.rolloversphere.objects.boxes.factory.IBoxesFactory;

public class FreeplayGame extends TheGame{

	public FreeplayGame(Game game) {
		super(game);		
	}

	@Override
	protected IBoxesFactory getBoxesFactory() {		
		return new FreeplayBoxesFactory();
	}

	@Override
	protected void renderGameSpecific(float timeDiff) {
		mSpriteBatch.begin();
		//draw player points
		mFont.setColor(Color.WHITE);
		mFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		mFont.draw(mSpriteBatch, this.getPointsString(), getScreenX(5.0f), getScreenY(5.0f));	
		
		upgradeTimeoutUpdateAndRender(timeDiff);
		renderNotificationText(timeDiff, 0.9f, 0.5f, NotifStates.NOT_SHOW);
		
		mPointsManager.update(timeDiff);	
		mSpriteBatch.end();	
	}
	
	float time = 0.0f;
	float dec = 0.1f;
	float sleep = 1.2f;
	@Override
	protected void addNewBox(float timeDiff) {
		time += timeDiff;
		if (time >= sleep) {
			mBoxesManager.addNew(mChuckSphere.position, mChuckSphere.boundingSphereR);
			time = 0.0f;
			if (sleep > 0.1f) {
				sleep -= dec;
			}
		}
	}

	@Override
	protected GameTypes getGameType() {
		return GameTypes.FREEPLAY;
	}
}
