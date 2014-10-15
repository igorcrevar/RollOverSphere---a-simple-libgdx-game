package com.igorcrevar.rolloverchuck.scenes;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Sphere;
import com.igorcrevar.rolloverchuck.GameConsts;
import com.igorcrevar.rolloverchuck.GameData;
import com.igorcrevar.rolloverchuck.GameManager;
import com.igorcrevar.rolloverchuck.IActivityRequestHandler;
import com.igorcrevar.rolloverchuck.IScene;
import com.igorcrevar.rolloverchuck.ISceneManager;
import com.igorcrevar.rolloverchuck.ISceneManager.GameState;
import com.igorcrevar.rolloverchuck.ISceneManager.Type;
import com.igorcrevar.rolloverchuck.objects.ChuckObject;
import com.igorcrevar.rolloverchuck.objects.BoxObject;
import com.igorcrevar.rolloverchuck.objects.FieldObject;
import com.igorcrevar.rolloverchuck.objects.boxes.BoxManager;
import com.igorcrevar.rolloverchuck.objects.boxes.BoxRegionManager;
import com.igorcrevar.rolloverchuck.objects.boxes.BoxTypeFactory;
import com.igorcrevar.rolloverchuck.objects.boxes.IBoxRegion;
import com.igorcrevar.rolloverchuck.physics.CollisionSolver;
import com.igorcrevar.rolloverchuck.physics.ICollisionIterationHandler;
import com.igorcrevar.rolloverchuck.points.PointsManager;
import com.igorcrevar.rolloverchuck.scenes.GameMode.IGameMode;
import com.igorcrevar.rolloverchuck.utils.GameHelper;
import com.igorcrevar.rolloverchuck.utils.Mathf;

public class GameScene implements IScene, InputProcessor, ICollisionIterationHandler, IActivityRequestHandler.IConfirmDialogCallback {	
	private Matrix4 projViewMatrix;
	private Matrix4 viewMatrix;
	private Vector3 lightPos;
	
	private Matrix4 tmpProjViewMatrix = new Matrix4();
	private Matrix4 tmpViewMatrix = new Matrix4();
	private Vector3 tmpVertex = new Vector3();
	
	private GameManager gameManager;
	private ISceneManager sceneManager;
	private FieldObject field;
	private ChuckObject chuckObject;
	
	// (X, Y, pointer)
	private Vector3 lastTouchPointer = new Vector3();
	// we want to prevent moving to mouch with one touch 
	private float touchMovingDistance;

	// collision solver
	private CollisionSolver collisionSolver = new CollisionSolver();
	
	private SpriteBatch spriteBatch = new SpriteBatch();
	
	private PointsManager pointsManager = new PointsManager();
	
	private BoxManager boxManager;
	private BoxRegionManager boxRegionManager;
	
	private Sphere boundingSphere1 = new Sphere(Vector3.X, 0.0f);
	private Sphere boundingSphere2 = new Sphere(Vector3.X, 0.0f);
	
	private Random random = new Random();
	private float timer = 0;
	private float lastTimeBoxPickUp;
	private int pointsMulti;
	private float timeBoxGenerated;
	private float timeBonusBoxGenerated;
	private float timeNegativeBoxGenerated;
	
	private boolean shouldReturnToPrevScene;
	private IGameMode gameMode;
	private Sprite pointImage = new Sprite();
	
	public GameScene(ISceneManager sceneManager) {
		this.sceneManager = sceneManager;
		this.gameManager = sceneManager.getGameManager();
		
		float projAspect = (float)Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		projViewMatrix = new Matrix4().setToProjection(0.1f, 1000.0f, 35.0f, projAspect);
		viewMatrix = new Matrix4().setToLookAt(new Vector3(0.0f, 34.0f, 30.0f), new Vector3(0.0f, 29.0f, 24.0f), Vector3.Y);
		projViewMatrix.mul(viewMatrix);
		
		lightPos = new Vector3(20.0f, 40.0f, 36.0f);
		
		field = new FieldObject(gameManager);
		chuckObject = new ChuckObject(gameManager);
		
		boxManager = new BoxManager(gameManager);
		boxRegionManager = new BoxRegionManager(gameManager.getGameData());
		
		pointImage.setRegion(gameManager.getTextureAtlas("base").findRegion("point"));
		pointImage.setSize(60, 60);
		pointImage.setPosition(40, 850);
	}
	
	@Override
	public void init(ISceneManager sceneManager) {
		Gdx.input.setInputProcessor(this);
		// Use culling to remove back faces.
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);		 
		Gdx.gl.glCullFace(GL20.GL_BACK);
		// Enable depth testing
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
				
		pointsManager.init();
		chuckObject.init();
		
		boxManager.init();
		boxRegionManager.init();
		
		timer = 0;
		lastTimeBoxPickUp = 0.0f;
		pointsMulti = 1;
		shouldReturnToPrevScene = false;
		
		sceneManager.getGameManager().getStars().init(sceneManager.getGameManager().getTextureAtlas("base").findRegion("star"));
		
		// box generator 
		timeBoxGenerated = -10000f; // Immediately normal box should be generated
		timeNegativeBoxGenerated = 0.0f;
		timeBonusBoxGenerated = 0.0f;
	}

	@Override
	public void update(ISceneManager sceneManager, float deltaTime) {
		if (gameMode == null) {
			throw new IllegalArgumentException("Game mode not set!");
		}
		
		if (sceneManager.isGamePaused()) {
			return;
		}			
		
		if (shouldReturnToPrevScene) {
			// score/etc will be saved only if player played game at least for nnn long
			sceneManager.finishGame(timer > sceneManager.getGameManager().getGameData().MinTimeCountAsOnePlay);
			sceneManager.setScene(Type.IntroScene);
			return;
		}
		
		if (sceneManager.getGameState() == GameState.COUNTING) {
			updateCounting(deltaTime);
		}
		else {
			updateWorld(deltaTime);
		}
		
		// draw
		GameHelper.clearScreen();
		
		GameHelper.setProjectionFor2D(spriteBatch, 1920, 1080);
		spriteBatch.begin();
		sceneManager.getGameManager().getStars().update(deltaTime);
		sceneManager.getGameManager().getStars().draw(spriteBatch);
		spriteBatch.end();		
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		// draw field
		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);		
		tmpProjViewMatrix.set(projViewMatrix);
		field.draw(tmpProjViewMatrix);
		
		// draw shadows
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		tmpProjViewMatrix.set(projViewMatrix);
		ShaderProgram sp = gameManager.getShader("shadow");
		sp.begin();
		sp.setUniformf(GameConsts.LightPosName, lightPos);
		sp.setUniformf("u_planeY", 0.0f + GameData.ObjectZero.y);
		chuckObject.drawShadow(sp, tmpProjViewMatrix);
		sp.end();
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		// chuck
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		tmpProjViewMatrix.set(projViewMatrix);
		tmpViewMatrix.set(viewMatrix);
		chuckObject.draw(tmpProjViewMatrix, tmpViewMatrix, lightPos);
		
		// boxes 
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		tmpProjViewMatrix.set(projViewMatrix);
		tmpViewMatrix.set(viewMatrix);
		boxManager.draw(tmpProjViewMatrix, tmpViewMatrix, lightPos);
		
		// draw points
		GameHelper.setProjectionFor2D(spriteBatch, 1920, 1080);
		spriteBatch.begin();
		pointImage.draw(spriteBatch);
		BitmapFont font = gameManager.getBitmapFont();
		font.setScale(0.8f);
		pointsManager.draw(spriteBatch, font);		
		font.setColor(Color.WHITE);
		font.draw(spriteBatch, GameHelper.getStringPoints(sceneManager.getCurrentScore()), 120, 900);
		if (sceneManager.getGameState() == GameState.COUNTING) {
			int valInt = (int)(4 - timer * 2);
			font.draw(spriteBatch, valInt > 0 ? String.valueOf(valInt) : "Go!", valInt > 0 ? 940 : 920, 940);
			font.setColor(Color.BLACK);
			String txt = "Slide to move ball!";
			font.draw(spriteBatch, txt, GameHelper.getAlignedPosX(font, txt, 1920f) - 20f, 805f);
			font.setColor(Color.WHITE);
			font.draw(spriteBatch, txt, GameHelper.getAlignedPosX(font, txt, 1920f), 800f);
		}
		gameMode.drawSprites(font, spriteBatch);
		spriteBatch.end();
		gameMode.postUpdate(spriteBatch);
	}

	@Override
	public void dispose(ISceneManager sceneManager) {
		spriteBatch.dispose();
		gameMode.dispose();
	}

	@Override
	public void leave(ISceneManager sceneManager) {
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.HOME) {
			Gdx.app.log("chuck", "GameScene home pressed");
	    }
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			if (sceneManager.isGameActive()) {
				sceneManager.setGameState(GameState.PAUSED);
				sceneManager.getRequestHandler().confirmDialog("Quit current game?", this);
			}
			else {
				shouldReturnToPrevScene = true;
			}
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		lastTouchPointer.set(screenX, screenY, pointer);
		touchMovingDistance = 0f;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (sceneManager.isGameOver()) {
			gameMode.touchUp(screenX, screenY, pointer, button);
			return true;
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (sceneManager.isGameActive() && touchMovingDistance < gameManager.getGameData().ChuckAcc) {
			float dx = screenX - lastTouchPointer.x;
			float dz = screenY - lastTouchPointer.y;
			float incX = dx / Gdx.graphics.getWidth() * gameManager.getGameData().ChuckAcc;
			float incZ = dz / Gdx.graphics.getHeight() * gameManager.getGameData().ChuckAcc;
			touchMovingDistance += Math.sqrt(incX * incX + incZ * incZ);
			chuckObject.addMoving(incX, incZ);
			lastTouchPointer.set(screenX, screenY, pointer);
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	@Override
	public void confirmDialogResult(boolean result) {
		sceneManager.setGameState(GameState.Active);
		shouldReturnToPrevScene = result;		
	}

	// called after every step of "physics" engine
	@Override
	public boolean iterationHandler(Vector3 position, float time, Object tag) {
		// set chuck bounding box and radius
		boundingSphere1.center.set(position);
		boundingSphere1.radius = Mathf.lerp(chuckObject.getPrevFrameRadius(), chuckObject.getCurrentRadius(), time);
		
		int boxIndex = boxManager.getAvailableCount() - 1;
		BoxObject boxObject;
		while ((boxObject = boxManager.getBoxObject(boxIndex)) != null) {
			// populate bounding sphere for box
			boundingSphere2.center.set(boxObject.GetPosition());
			boundingSphere2.radius = boxObject.getScale() * 0.5f;
			// check collision between two spheres
			if (boundingSphere1.overlaps(boundingSphere2)) {								
				// add points or power ups
				int points = boxObject.getPoints();
				if (points > 0) {
					// point multiplicator!
					// if player pick up points really fast than points are multiplicated every time
					if (timer - lastTimeBoxPickUp < gameManager.getGameData().PointMultiplicatorTime) {
						pointsMulti = Math.min(pointsMulti * 2, gameManager.getGameData().PointMaxMultiplicator);
					}
					else {
						// not picked fast enough - reset multiplicator
						pointsMulti = 1;
					}
					// add score and update point manager
					addScore(points > 1 ? pointsMulti + points - 1 : pointsMulti, boxObject);
					// update time for multi bonus
					lastTimeBoxPickUp = timer;
					// remove collided box from available boxes list
					boxManager.remove(boxIndex);
					// generate new box immediately after picking up positive
					generateBox(0);
				}
				else if (points < 0) {
					// reset multi
					pointsMulti = 1;
					// add score
					addScore(points, boxObject);
					// remove collided box from available boxes list
					boxManager.remove(boxIndex);
				}
				else {
					boxObject.applyUpgrade(chuckObject);
					// remove collided box from available boxes list
					boxManager.remove(boxIndex);
				}
			}
			
			--boxIndex;
		}
		
		return false;
	}
	
	private void generateBox(int type) {
		IBoxRegion br = boxRegionManager.getOne(chuckObject.getCurrentPosition(), gameManager.getGameData().BoxDistanceSquared);
		if (br != null) {
			boxManager.add(br, gameManager.getGameData().BoxTimeToLive, BoxTypeFactory.create(type));
		}	
	}
	
	private void updateWorld(float deltaTime) {
		// generation of new boxes - only if game is active
		if (sceneManager.isGameActive()) {
			// standard box
			if (timer - timeBoxGenerated >= gameManager.getGameData().BoxGenerationTime) {
				timeBoxGenerated = timer;
				generateBox(0);
				
				// power ups (grow up/shrink) are (almost) completely random!
				// They can only be generated when new box is generated
				int val = random.nextInt(40);
				if (val == 5) {
					generateBox(1);
				}
				else if (val == 10) {
					generateBox(2);
				}
			}
			
			// bonus box
			if (timer - timeBonusBoxGenerated >= gameManager.getGameData().BoxBonusBoxGenerationTime) {
				timeBonusBoxGenerated = timer;
				generateBox(3);
			}
			
			// negative box
			if (timer - timeNegativeBoxGenerated >= gameManager.getGameData().BoxNegativeBoxGenerationTime) {
				timeNegativeBoxGenerated = timer;
				generateBox(4);
			}
		}
		
		// chuck update
		chuckObject.update(deltaTime);
		// boxes update
		boxManager.update(deltaTime);				
		if (sceneManager.isGameActive()) {
			// detect collisions only if game is active !
			collisionSolver.iterateOver(this, chuckObject.getPrevFramePosition(), chuckObject.getCurrentPosition(), null);
		}		
		// update points
		pointsManager.update(deltaTime);
		// game mode update
		gameMode.update(timer, deltaTime);
		// icrement timer
		timer += deltaTime;
	}
	
	private void updateCounting(float deltaTime) {
		// chuck update
		chuckObject.update(deltaTime);
		
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		timer += deltaTime;
		if (timer >= 2.0f) {
			timer = 0f;
			sceneManager.setGameState(GameState.Active);
			gameManager.playGameMusic();
		}
	}
	
	private void addScore(int pointIncrement, BoxObject boxObject) {		
		tmpVertex.set(boxObject.GetPosition()).add(GameData.ObjectZero).add(0f, 0.5f * boxObject.getScale(), 0.0f);
		tmpVertex.prj(projViewMatrix);
		// 1920 x 1080 is default virtual screen				
		float x = 1920f * (tmpVertex.x + 1) / 2;
		float y = 1080f * (tmpVertex.y + 1) / 2;
		pointsManager.add(pointIncrement, x - 20.0f, y + 30.0f);
		
		// update points in scene manager
		sceneManager.addToScore(pointIncrement);
	}

	public void setGameMode(IGameMode gameMode) {
		this.gameMode = gameMode;
		field.setTexture(this.gameMode.getFieldTextureRegion());
	}
}
