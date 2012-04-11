package com.igorcrevar.rolloversphere.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.igorcrevar.rolloversphere.collsion.CollisionSolver;
import com.igorcrevar.rolloversphere.game.SettingsHelper;
import com.igorcrevar.rolloversphere.input.IMyInputAdapter;
import com.igorcrevar.rolloversphere.input.MyInputAdapter;
import com.igorcrevar.rolloversphere.objects.ChuckSphere;
import com.igorcrevar.rolloversphere.objects.DynamicPlane;
import com.igorcrevar.rolloversphere.objects.Floor;
import com.igorcrevar.rolloversphere.objects.PointsManager;
import com.igorcrevar.rolloversphere.objects.boxes.BoxType;
import com.igorcrevar.rolloversphere.objects.boxes.BoxesManager;
import com.igorcrevar.rolloversphere.objects.boxes.factory.IBoxesFactory;

public abstract class TheGame implements Screen, IMyInputAdapter{
	protected final ChuckSphere mMainBall = new ChuckSphere();
	protected final DynamicPlane mWaterBackground = new DynamicPlane();
	protected final Floor mFloor = new Floor();
	protected long mLastTime;
	protected BoxesManager mBoxesManager;
	protected PointsManager mPointsManager;
	protected BitmapFont mFont;
	
	protected SpriteBatch mSpriteBatch;	
	protected PerspectiveCamera mCamera;
	protected Vector3 mLastBallPos;
	
	//upgrade
	protected BoxType mUpgradeType = BoxType.NOT_UPGRADE;
	protected long mUpgradeTimeStarted;
	protected int mUpgradeTimeout;
	//TODO: budz!!!!
	protected enum NotifStates{
		FADE_IN, FADE_OUT, NOT_SHOW
	}
	protected String mNotifText;
	protected Color mNotifTextColor;
	protected NotifStates mNotifTextAnimationState; //0 - fade in 1 - fade out, 2 - not show
	
	private MyInputAdapter mInputAdapter;
	protected Game mGame;
	protected enum GameStatus{
		PLAY, GAMEOVER, EXIT
	}
	protected GameStatus mGameStatus;
	protected GameStatus mLastGameStatusBeforePause;
	protected Thread mBoxThread;
	
	protected float mNormalSpeedDivider;
	protected float mFastSpeedDivider;
	
	public TheGame(Game game) {
		this.mGame = game;
		mInputAdapter = new MyInputAdapter(this);
		mGameStatus = GameStatus.PLAY;
		mNormalSpeedDivider = 6.0f;
		mFastSpeedDivider = 4.0f;
		init();
	}
	
	//override it!
	protected abstract IBoxesFactory getBoxesFactory();
	
	private void init(){
		mNotifTextColor = Color.WHITE;
		mNotifTextAnimationState = NotifStates.NOT_SHOW;
		mBoxesManager = new BoxesManager(getBoxesFactory());
		
		mSpriteBatch = new SpriteBatch();
		mFont = new BitmapFont();		
		mPointsManager = new PointsManager(mFont, mSpriteBatch);
		
		mBoxesManager.init(-30, 30, -80, 0);
		
		mWaterBackground.init();
		mMainBall.init();
		mFloor.init();
		
		mMainBall.minX = -30;
		mMainBall.maxX = 30;
		mMainBall.maxZ = 0;
		mMainBall.minZ = -80;
		
		mWaterBackground.rotation.x = 90;
		mWaterBackground.position.x = 4;
		mWaterBackground.position.y = 15;
		mWaterBackground.position.z = -80.0f;
		
		mFloor.position.y = -mMainBall.boundingSphereR;
		mFloor.position.z = -40.0f;
		
		Gdx.input.setInputProcessor(new MyInputAdapter(this));
		
		mBoxThread = getBoxThread();
	}
	
	private float getTimeDiff(){
		long time = System.currentTimeMillis();
		if (mLastTime == 0){
			mLastTime = time;
			return 0;
		}
		
		float timeDiff = (float)((time - mLastTime) / 1000.0);
		mLastTime = time;
		return timeDiff;
	}
	
	protected void createNotification() {
		mNotifText = mUpgradeType.getText();
		mNotifTextColor.a = 0.0f;
		mNotifTextAnimationState = NotifStates.FADE_IN;
	}
	
	protected void renderNotificationText(float timeDiff, float inSpeed, float outSpeed, NotifStates afterFinish){
		if (mNotifTextAnimationState != NotifStates.NOT_SHOW){
			drawInMiddle(mNotifText, mNotifTextColor);
			if (mNotifTextAnimationState == NotifStates.FADE_IN){
				mNotifTextColor.a += timeDiff * inSpeed;
				if (mNotifTextColor.a >= 1.0f){
					mNotifTextAnimationState = NotifStates.FADE_OUT;
					mNotifTextColor.a = 1.0f;
				}
			}
			else{
				mNotifTextColor.a -= timeDiff * outSpeed;
				if (mNotifTextColor.a <= 0.0f){
					mNotifTextAnimationState = afterFinish;
					mNotifTextColor.a = 0.0f;
				}
			}			
		}
	}
	
	protected void drawInMiddle(String str, Color color){
		TextBounds bounds = mFont.getBounds(str);
		float x = (Gdx.graphics.getWidth() - bounds.width) / 2;
		float y = Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() - bounds.height) / 2;
		//mFont.setScale(2.2f * Gdx.graphics.getWidth() / 800);
		mFont.setColor(color);
		mFont.draw(mSpriteBatch, str, x, y);  
	}
	
	protected void upgradeTimeoutUpdateAndRender(){
		//upgrade part
		if (mUpgradeType != BoxType.NOT_UPGRADE){
			int secs = (int)((mLastTime - mUpgradeTimeStarted) / 1000);
			if (secs > mUpgradeTimeout){
				//if timeout expires than we are not in upgrade state anymore
				mUpgradeType = BoxType.NOT_UPGRADE;
			}
			else{
				String str = String.format("Upgrade: %d", mUpgradeTimeout - secs);
				mFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
				mFont.draw(mSpriteBatch, str, getScreenXEnd(200.0f), getScreenY(5.0f));
			}
		}
	}
	
	private float fixSpeed(float v){
		if (v > 480.0f){
			return 480.0f;
		}
		if (v < -480.0f){
			return -480.0f;
		}
		return v;
	}
	
	private void killBoxThread(){
		mGameStatus = GameStatus.EXIT;
		while (mBoxThread.isAlive());
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void hide() {
		killBoxThread();		
	}

	@Override
	public void pause() {
		mLastGameStatusBeforePause = mGameStatus;
		killBoxThread();
	}

	@Override
	public void render(float deltaTimeDoNotUse){
		float timeDiff = getTimeDiff();
		Gdx.gl.glClear(GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);

		mWaterBackground.update(timeDiff);
		mLastBallPos = mMainBall.position;
		mMainBall.update(timeDiff);
				
		mWaterBackground.render(mCamera);	    
		mFloor.render(mCamera);
		
		renderGameSpecific(timeDiff);
		
		if (Gdx.app.getType() ==  com.badlogic.gdx.Application.ApplicationType.Android  
			&& SettingsHelper.isAcceleatorEnabled) {
			//todo: dirty fix
			float dx = Gdx.input.getAccelerometerX();
			float dy = Gdx.input.getAccelerometerY();
			if (mUpgradeType != BoxType.UPGRADE_SPEED){
				dx = dx * mNormalSpeedDivider;
				dy = dy * mNormalSpeedDivider;
			}
			else{
				dx = dx * mFastSpeedDivider;
				dy = dy * mFastSpeedDivider;
			}
			updatePlayPosition(dx, dy);
		}
	}

	protected abstract Thread getBoxThread();
	protected abstract void renderGameSpecific(float timeDiff);

	@Override
	public void resize(int width, int height) {
		// Sets the current view port to the new size.
		Gdx.gl.glViewport(0, 0, width, height);
		mCamera = new PerspectiveCamera(60.0f, width, height);
		mCamera.rotate(-30.0f, 1.0f, 0.0f, 0.0f);		
		mCamera.far += 50.0f;
		
		mCamera.position.set(0, 40.0f, 30.0f);
		mCamera.update();
		
		if (width >= 800){
			mFont.setScale(2.0f);	
		}
		else if (width >= 480){
			mFont.setScale(1.2f);
		}
		else{
			mFont.setScale(1.0f);
		}
		
		setOpenGlDefault();
	}
	
	private void setOpenGlDefault(){
		Gdx.gl.glClearColor(0f, 0, 0, 1f);		
		Gdx.gl.glClearDepthf(1.0f);
		// Enables depth testing.
		// The type of depth testing to do.
		Gdx.gl.glDepthFunc(GL10.GL_LEQUAL);		
	}
	
	private void setInputAndStartBoxThread(){
		Gdx.input.setInputProcessor(mInputAdapter);
		Gdx.input.setCatchBackKey(true);
		try
		{
			mBoxThread.start();
		}
		catch(IllegalThreadStateException ex){
			//thread is already started - do nothing
		}
	}

	@Override
	public void resume() {
		setInputAndStartBoxThread();
		setOpenGlDefault();
		mGameStatus = mLastGameStatusBeforePause;
	}

	@Override
	public void show() {
		setInputAndStartBoxThread();
	}
	
	private void updatePlayPosition(float dx, float dy){
		if (mGameStatus != GameStatus.PLAY){
			return;
		}
		
		//todo: shrinking values of dx and dy are very dirty
		switch (mUpgradeType){
		case NOT_UPGRADE:
			dx = dx / mNormalSpeedDivider;
			dy = dy / mNormalSpeedDivider;
			
			mMainBall.friction = ChuckSphere.DEFAULT_FRICTION;
			mMainBall.movingVelocity.x = fixSpeed(mMainBall.movingVelocity.x + dx);
			mMainBall.movingVelocity.z = fixSpeed(mMainBall.movingVelocity.z + dy);
			break;
		case UPGRADE_EASY_CHANGE_DIRECTION:
			dx = dx / mNormalSpeedDivider;
			dy = dy / mNormalSpeedDivider;
			
			int oldSign = CollisionSolver.getSign(mMainBall.movingVelocity.x);
			int incSign = CollisionSolver.getSign(dx);
			//Math.abs(dx) > 2.0f  &&  
			if (Math.abs(oldSign - incSign) == 2){
				mMainBall.movingVelocity.x = 0.0f;// -mMainBall.movingVelocity.x;
			}
			
			oldSign = CollisionSolver.getSign(mMainBall.movingVelocity.y);
			incSign = CollisionSolver.getSign(dy);
			if (Math.abs(oldSign - incSign) == 2){
				mMainBall.movingVelocity.y = 0.0f; //-mMainBall.movingVelocity.y;
			}
			
			mMainBall.friction = ChuckSphere.DEFAULT_FRICTION;
			mMainBall.movingVelocity.x = fixSpeed(mMainBall.movingVelocity.x + dx);
			mMainBall.movingVelocity.z = fixSpeed(mMainBall.movingVelocity.z + dy);
			break;
		case UPGRADE_SPEED:
			dx = dx / mFastSpeedDivider;
			dy = dy / mFastSpeedDivider;
			
			mMainBall.friction = ChuckSphere.DEFAULT_FRICTION;
			mMainBall.movingVelocity.x = fixSpeed(mMainBall.movingVelocity.x + dx);
			mMainBall.movingVelocity.z = fixSpeed(mMainBall.movingVelocity.z + dy);
			break;
		}
	}
	
	@Override
	public void onMove(int dx, int dy, int x, int y) {
		updatePlayPosition(dx, dy);
	}

	@Override
	public void onClick(int x, int y) {	
	}

	@Override
	public void onDoubleClick(int x, int y) {
		if (mGameStatus == GameStatus.PLAY){
			if (mMainBall.movingVelocity.z < 0){
				mMainBall.movingVelocity.z = fixSpeed(mMainBall.movingVelocity.z - 120.0f);
			}
			else{
				mMainBall.movingVelocity.z = fixSpeed(mMainBall.movingVelocity.z + 120.0f);
			}
		}
	}

	@Override
	public void onBack(){
		mGame.setScreen(new MainMenuScreen(mGame));
	}
	
	protected float getScreenX(double x){
		double rv = x * Gdx.graphics.getWidth() / 800.0;
		return (float)rv;
	}
	
	protected float getScreenY(double y){
		double rv = Gdx.graphics.getHeight() - y * Gdx.graphics.getHeight() / 480.0;
		return (float)rv;
	}
	
	protected float getScreenXEnd(double x){
		double rv = Gdx.graphics.getWidth() - x * Gdx.graphics.getWidth() / 800.0;
		return (float)rv;
	}
	
	protected float getScreenYEnd(double y){
		double rv = y * Gdx.graphics.getHeight() / 480.0;
		return (float)rv;
	}
}
