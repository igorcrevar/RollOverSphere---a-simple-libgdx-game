package com.igorcrevar.rolloverchuck;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.igorcrevar.rolloverchuck.GameType.BaseGameType;
import com.igorcrevar.rolloverchuck.GameType.Difficulty;
import com.igorcrevar.rolloverchuck.mesh.CubeMesh;
import com.igorcrevar.rolloverchuck.mesh.CubeMeshWithNormals;
import com.igorcrevar.rolloverchuck.mesh.FieldMesh;
import com.igorcrevar.rolloverchuck.mesh.IMesh;
import com.igorcrevar.rolloverchuck.mesh.SphereMesh;
import com.igorcrevar.rolloverchuck.objects.StarsObject;
import com.igorcrevar.rolloverchuck.utils.ShaderAssetLoader;

public class GameManager implements Disposable {
	private AssetManager assetManager = new AssetManager();
	
	private boolean isSoundOn;
	
	private HashMap<String, IMesh> meshMap = new HashMap<String, IMesh>(8);	
	private GameData gameData = new GameData();
	private Preferences preferences;
	private StarsObject stars;

	private Sprite overlaySprite;
	
	public GameManager() {
		//load top score, etc
		preferences = Gdx.app.getPreferences("crewprefs");
		isSoundOn = preferences.getBoolean("sound_on", true);		
		
		assetManager.setLoader(ShaderProgram.class, new ShaderAssetLoader(new InternalFileHandleResolver()));
		
		// add all needed assets to queue
		assetManager.load("fonts/font.fnt", BitmapFont.class);
		assetManager.load("atlases/widgets.atlas", TextureAtlas.class);
		assetManager.load("atlases/base.atlas", TextureAtlas.class);
		assetManager.load("sounds/intro.ogg", Music.class);
		assetManager.load("sounds/game.ogg", Music.class);
		assetManager.load("light", ShaderProgram.class);
		assetManager.load("field", ShaderProgram.class);
		assetManager.load("simple", ShaderProgram.class);
		assetManager.load("shadow", ShaderProgram.class);
		assetManager.load("region", ShaderProgram.class);
	}
	
	public void setSoundOn(boolean value) {
		isSoundOn = value;
		preferences.putBoolean("sound_on", value);
		preferences.flush();
		if (isSoundOn) {
			playIntroMusic();
		}
		else {
			stopIntroMusic();
		}
	}
	
	public void saveScore(GameType gameType, long score) {
		// games played by player
		String gamesPlayedKey = "games_played" + gameType.toString();
		preferences.putLong(gamesPlayedKey, preferences.getLong(gamesPlayedKey, 0) + 1);
		// save top score
		String topScoreKey = "top_score" + gameType.toString();
		long topScore = preferences.getLong(topScoreKey, 0);
		if (topScore < score) {
			preferences.putLong(topScoreKey, score);
		}
		
		preferences.flush();
	}
	
	public long getTopScore(GameType gameType) {
		return preferences.getLong("top_score" + gameType.toString(), 0);
	}
		
	public long getTotalGamesPlayed(GameType gameType) {
		return preferences.getLong("games_played" + gameType.toString(), 0);
	}
	
	public long getAllGamesPlayed() {
		long res = 0;
		for (BaseGameType baseType : BaseGameType.values()) {
			for (Difficulty difficulty : Difficulty.values()) {
				String key = String.format("games_played%d-%d", baseType.ordinal(), difficulty.ordinal());
				res += preferences.getLong(key, 0);
			}
		}
		
		return res;
	}
	
	public boolean getIsSoundOn() {
		return isSoundOn;
	}
	
	/**
	 * Update loading of assets
	 * @return true if loading is finished
	 */
	public boolean updateLoading() {
		if (assetManager.update()) {
			// add meshes
			addMesh("plane", new FieldMesh(gameData));
			addMesh("sphere", new SphereMesh(gameData));
			addMesh("cube", new CubeMesh(gameData));
			addMesh("cube_with_normals", new CubeMeshWithNormals(gameData));
			return true;
		}		
		return false;
	}
	
	@Override
	public void dispose() {
		assetManager.dispose();		
		for (IMesh m : meshMap.values()) {
			m.dispose();
		}
	}
	
	public TextureAtlas getTextureAtlas(String fileName) {
		String fullName = "atlases/" + fileName + ".atlas";
		return assetManager.get(fullName, TextureAtlas.class);
	}
	
	public Texture getTexture(String fileName) {
		return assetManager.get("images/" + fileName, Texture.class);
	}
	
	public Sound getSound(String fileName) {
		return assetManager.get("sounds/" + fileName, Sound.class);
	}
	
	public ShaderProgram getShader(final String fileName) {
		return assetManager.get(fileName, ShaderProgram.class);
	}
	
	public void addMesh(final String name, IMesh mesh) {
		meshMap.put(name, mesh);
	}
	
	public IMesh getMesh(final String name) {
		return meshMap.get(name);
	}
	
	public void playIntroMusic() {
		if (isSoundOn && assetManager.isLoaded("sounds/intro.ogg", Music.class)) {
			Music music = assetManager.get("sounds/intro.ogg", Music.class);
			music.setLooping(true);
			music.play();
		}
	}
	
	public void stopIntroMusic() {
		if (assetManager.isLoaded("sounds/intro.ogg", Music.class)) {
			Music music = assetManager.get("sounds/intro.ogg", Music.class);
			music.stop();
		}
	}
	
	public void playGameMusic() {
		if (isSoundOn && assetManager.isLoaded("sounds/game.ogg", Music.class)) {
			Music music = assetManager.get("sounds/game.ogg", Music.class);
			music.setLooping(false);
			music.play();
		}
	}
	
	public void stopGameMusic() {
		if (assetManager.isLoaded("sounds/game.ogg", Music.class)) {
			Music music = assetManager.get("sounds/game.ogg", Music.class);
			music.stop();
		}
	}
	
	public void playCoinSound() {
		if (isSoundOn) {
			getSound("coin.wav").play();
		}
	}
	
	public void playMoveSound() {
		if (isSoundOn) {
			getSound("move.wav").play();
		}
	}
	
	public void playDieSound() {
		if (isSoundOn) {
			getSound("die.wav").play();
		}
	}
	
	public BitmapFont getBitmapFont() {
		return assetManager.get("fonts/font.fnt", BitmapFont.class);
	}
	
	public boolean isBitmapFontLoaded() {
		return assetManager.isLoaded("fonts/font.fnt", BitmapFont.class);
	}
	
	public GameData getGameData() {
		return gameData;
	}
	
	public StarsObject getStars() {
		if (stars == null) {
			stars = new StarsObject();
		}
		
		return stars;
	}
	
	public Sprite getOverlaySprite(float rgbFactor, float alphaFactor) {
		if (overlaySprite == null) {
			overlaySprite = new Sprite();
			overlaySprite.setBounds(0, 0, 1920, 1080);
			overlaySprite.setOrigin(960, 540);
			overlaySprite.setRegion(getTextureAtlas("base").findRegion("myfont"));
		}
				
		overlaySprite.setColor(rgbFactor, rgbFactor, rgbFactor, alphaFactor);
		return overlaySprite;
	}
}