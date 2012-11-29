package com.igorcrevar.rolloversphere.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.igorcrevar.rolloversphere.game.GameTypes;
import com.igorcrevar.rolloversphere.game.SettingsHelper;
import com.igorcrevar.rolloversphere.objects.boxes.factory.ArcadeBoxesFactory;
import com.igorcrevar.rolloversphere.objects.boxes.factory.IBoxesFactory;

public class ArcadeGame extends TheGame {
	private float mGameTimer;

	public ArcadeGame(Game game) {
		super(game);
		mGameTimer = 0;
	}

	@Override
	protected IBoxesFactory getBoxesFactory() {
		return new ArcadeBoxesFactory();
	}

	@Override
	protected void renderGameSpecific(float timeDiff) {
		mSpriteBatch.begin();
		// draw player points
		mFont.setColor(Color.WHITE);
		mFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);		
		mFont.draw(mSpriteBatch, this.getPointsString(), getScreenX(5.0f), getScreenY(5.0f));

		if (GameStatus.PLAY == mGameStatus) {
			mGameTimer += timeDiff;
			if (mGameTimer >= SettingsHelper.arcadeGameTimeout) {
				mGameStatus = GameStatus.GAMEOVER;
				mNotifTextAnimationState = NotifStates.FADE_IN;
				mNotifTextColor = Color.RED;
				mNotifTextColor.a = 0.0f;
			} else {
				int time = (int) (SettingsHelper.arcadeGameTimeout - mGameTimer);
				String str = "Time: " + time; 
				mFont.draw(mSpriteBatch, str, getScreenX(5.0f),
						getScreenY(40.0f));
			}

			upgradeTimeoutUpdateAndRender(timeDiff);
			renderNotificationText(timeDiff, 0.9f, 0.5f, NotifStates.NOT_SHOW);
		} else {
			mGameOverTimer += timeDiff;
			// prevent overflow
			if (mGameOverTimer > 100.0f) {
				mGameOverTimer = mGameOverTimer - 100.0f;
			}
			mNotifText = "Time's up!";
			renderNotificationText(timeDiff, 2.2f, 2.0f, NotifStates.FADE_IN);
		}

		mPointsManager.update(timeDiff);
		mSpriteBatch.end();
	}

	@Override
	protected GameTypes getGameType() {
		return GameTypes.ARCADE;
	}

	float time = 0.0f;
	@Override
	protected void addNewBox(float timeDiff) {
		time += timeDiff;
		if (time >= 0.3f) {
			mBoxesManager.addNew(mChuckSphere.position, mChuckSphere.boundingSphereR);
			time = 0.0f;
		}
	}

}
