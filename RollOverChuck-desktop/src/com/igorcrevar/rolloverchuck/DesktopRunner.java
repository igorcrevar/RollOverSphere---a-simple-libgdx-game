package com.igorcrevar.rolloverchuck;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class DesktopRunner {
	public static void main(String[] arg) {
		int i = 0;
		if (i == 1) {
			createAtlas();
		}
		else if (i == 2) {
			createAtlasWidgets();
		}
		else {
			runGame();
		}
	}
	
	private static void createAtlasWidgets() {
		Settings settings = new Settings();
		settings.minHeight = 512;
		settings.minWidth = 512;
		settings.maxHeight = 512;
		settings.maxWidth = 512;
		settings.paddingY = 2;
		settings.paddingX = 2;
		settings.wrapY = TextureWrap.Repeat;
		TexturePacker2.process(settings, 
				//"D:\\gamepictures\\widgets\\",
				"D:\\gamepictures\\rollover\\widgets",				
				"D:\\MySelf\\Android\\workspacerollover\\RollOverChuck-android\\assets\\atlases", "widgets");
	}
	
	private static void createAtlas() {
		Settings settings = new Settings();
		settings.minHeight = 512;
		settings.minWidth = 512;
		settings.maxHeight = 512;
		settings.maxWidth = 512;
		settings.paddingY = 2;
		settings.paddingX = 2;
		settings.wrapY = TextureWrap.Repeat;
		TexturePacker2.process(settings, 
				"D:\\gamepictures\\rollover\\base",
				"D:\\MySelf\\Android\\workspacerollover\\RollOverChuck-android\\assets\\atlases", "base");
	}
	
	private static void runGame() {
		ApplicationListener gameListener = new GameListener(new IActivityRequestHandler() {			
			@Override
			public void rate() {
				System.out.println("rate");
			}

			@Override
			public void share() {
				System.out.println("share ");
			}

			@Override
			public void showAchievements() {
				System.out.println("showAchievements");
			}

			@Override
			public void showLeaderboard(GameType gameType) {
				System.out.println("showLeaderboards" + gameType.toString());
			}

			@Override
			public boolean getSignedInGPGS() {
				return true;
			}

			@Override
			public void loginGPGS() {
				System.out.println("loginGPGS");
			}

			@Override
			public void setGameManager(GameManager gameManager) {
				System.out.println("setGameManager");
			}

			@Override
			public void confirmDialog(final String txt,
					final IConfirmDialogCallback callback) {
				callback.confirmDialogResult(true);
			}

			@Override
			public void showAd() {
				System.out.println("show ad");
			}

			@Override
			public void updateLeaderboardAndAchievements() {
				System.out.println("updateLeaderboardAndAchievements called");
			}
		});
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Roll Over Chuck!";
		
		cfg.width = 1280;
		cfg.height = (int)(720.0f / 1280 * cfg.width);
		cfg.height = (int)(480.0f / 800 * cfg.width);
		
	    new LwjglApplication(gameListener, cfg);
	}
}
