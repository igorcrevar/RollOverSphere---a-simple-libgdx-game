package com.igorcrevar.rolloverchuck.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.igorcrevar.rolloverchuck.GameConsts;
import com.igorcrevar.rolloverchuck.GameData;
import com.igorcrevar.rolloverchuck.GameManager;
import com.igorcrevar.rolloverchuck.mesh.IMesh;
import com.igorcrevar.rolloverchuck.physics.SphereMoving;
import com.igorcrevar.rolloverchuck.utils.Mathf;

public class ChuckObject {	
	private IMesh mesh;
	private ShaderProgram sp;
	private Matrix4 modelMatrix;
	private GameData gameData;
	private SphereMoving sphereMoving = new SphereMoving();
	private Color colorFactor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	
	private float currentRadius;
	private float prevFrameRadius;
	private Vector3 prevFramePosition = new Vector3(); // we need because of physics
	
	// radius size change
	private float desiredRadius;
	private float radiusTimer;
	
	public ChuckObject(GameManager gameManager) {		
		mesh = gameManager.getMesh("sphere");
		sp = gameManager.getShader("light");
		modelMatrix = new Matrix4();
		
		gameData = gameManager.getGameData();
	}
	
	public void init() {
		sphereMoving.init(gameData.ChuckBoundaries, gameData.ChuckFriction, gameData.ChuckMaxVelocity, gameData.ChuckBouncingFactor);
		prevFramePosition.set(sphereMoving.getPosition());
		currentRadius = gameData.Chuck_r;
		radiusTimer = gameData.ChuckRadiusChangeTime + 0.1f;
	}
	
	public void reflect(boolean rX, boolean rY) {
		sphereMoving.reflect(rX, rY);
	}
	
	public void addMoving(float incX, float incZ) {
		sphereMoving.addVelocity(incX, incZ);
	}
	
	public void setMaxVelocity(Vector3 maxVelocity) {
		this.sphereMoving.setMaxVelocity(maxVelocity);
	}
	
	public void setFriction(Vector3 friction) {
		this.sphereMoving.setFriction(friction);
	}
	
	public void makeChuckBig() {
		desiredRadius = gameData.Chuck_rBig;
		radiusTimer = 0.0f;
	}
	
	public void makeChuckSmall() {
		desiredRadius = gameData.Chuck_rSmall;
		radiusTimer = 0.0f;
	}
	
	public void update(float deltaTime) {
		// remember old pos
		prevFramePosition.set(sphereMoving.getPosition());
		prevFrameRadius = currentRadius;
		
		// radius update if needed
		if (radiusTimer >= gameData.ChuckRadiusChangeTime) {
			currentRadius = gameData.Chuck_r;
		}
		else {
			currentRadius = Mathf.lerp(desiredRadius, gameData.Chuck_r, radiusTimer / gameData.ChuckRadiusChangeTime);
			radiusTimer += deltaTime;
		}
		
		// update moving / rotation / etc
		sphereMoving.update(deltaTime);
		// create model matrix
		modelMatrix.idt().translate(GameData.ObjectZero).translate(0.0f, gameData.Chuck_r, 0.0f);
		sphereMoving.populateMatrix(modelMatrix, currentRadius / gameData.Chuck_r);
	}
	
	public void draw(Matrix4 projViewMatrix, Matrix4 viewMatrix, Vector3 lightPos) {
		projViewMatrix.mul(modelMatrix);
		viewMatrix.mul(modelMatrix);
		sp.begin();
		sp.setUniformMatrix(GameConsts.ProjectionMatrixName,  projViewMatrix);
		sp.setUniformMatrix(GameConsts.ViewModelMatrixName, viewMatrix);
		sp.setUniformf(GameConsts.LightPosName, lightPos);
		sp.setUniformf("u_color_factor", colorFactor);		
		mesh.draw(sp);
		sp.end();
	}
	
	public void drawShadow(ShaderProgram sp, Matrix4 projViewMatrix) {
		sp.setUniformMatrix("u_modelMatrix", modelMatrix);
		sp.setUniformMatrix(GameConsts.ProjectionMatrixName,  projViewMatrix);
		mesh.draw(sp);		
	}	
	
	public Vector3 getPrevFramePosition() {
		return prevFramePosition;
	}
		
	public Vector3 getCurrentPosition() {
		return sphereMoving.getPosition();
	}
	
	public float getPrevFrameRadius() {
		return prevFrameRadius;
	}
	
	public float getCurrentRadius() {
		return currentRadius;
	}
}
