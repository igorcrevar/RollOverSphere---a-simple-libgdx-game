package com.igorcrevar.rolloversphere.game;

import com.badlogic.gdx.Game;
import com.igorcrevar.rolloversphere.game.screens.MainMenuScreen;

public class RollOverSphere extends Game{

	@Override
	public void create() {
		SettingsHelper.load();
		AssetsHelper.load();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		super.dispose();

		AssetsHelper.free();
		//SettingsHelper.save();
		if (getScreen() != null) {
			getScreen().dispose();
		}
	}
}
