package com.igorcrevar.rolloverchuck.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.igorcrevar.rolloverchuck.GameConsts;
import com.igorcrevar.rolloverchuck.GameData;
import com.igorcrevar.rolloverchuck.GameManager;
import com.igorcrevar.rolloverchuck.mesh.IMesh;
import com.igorcrevar.rolloverchuck.objects.boxes.IBoxRegion;
import com.igorcrevar.rolloverchuck.objects.boxes.IBoxType;
import com.igorcrevar.rolloverchuck.utils.Mathf;

public class BoxObject {
	private static final float MinAlpha = 0.2f;
	private static final float MaxAlpha = 0.9f;
	
	private IMesh mesh;
	private ShaderProgram sp;
	private Matrix4 modelMatrix;
	private Color color = new Color();
	private Vector3 position = new Vector3();
	private float angle;
	private float currentTime;
	private float timeToLive;
	
	private IBoxRegion boxRegion;
	private IBoxType boxType;
	private float baseScale;
	
	public BoxObject(GameManager gameManager) {
		mesh = gameManager.getMesh("cube_with_normals");
		sp = gameManager.getShader("light");
		modelMatrix = new Matrix4();
		baseScale = gameManager.getGameData().CubeScale;
	}
	
	public void init(IBoxRegion boxRegion, float timeToLive, IBoxType boxType) {
		this.boxRegion = boxRegion;
		this.boxRegion.populatePosition(this.position);
		this.boxRegion.take();
		
		this.color.set(boxType.getColor());
		this.color.a = MaxAlpha;
		this.timeToLive = timeToLive;
		this.boxType = boxType;
		this.currentTime = 0.0f;
	}
	
	public boolean update(float deltaTime) {
		color.a = Mathf.lerp(MaxAlpha, MinAlpha, currentTime / timeToLive);  
		
		float scale = boxType.getScale() * baseScale;
		modelMatrix.idt().translate(GameData.ObjectZero).translate(position.x, 0.5f * scale, position.z);
		modelMatrix.scale(scale, scale, scale);
		modelMatrix.mul(new Matrix4().rotate(com.badlogic.gdx.math.Vector3.Y, angle));
		
		currentTime += deltaTime;
		angle += boxType.getRotationSpeed() * deltaTime;
		return currentTime < timeToLive;
	}
	
	public void draw(Matrix4 projViewMatrix, Matrix4 viewMatrix, Vector3 lightPos) {
		projViewMatrix.mul(modelMatrix);		
		viewMatrix.mul(modelMatrix);
		sp.begin();
		sp.setUniformMatrix(GameConsts.ProjectionMatrixName,  projViewMatrix);
		sp.setUniformf("u_color_factor",  color);
		sp.setUniformMatrix(GameConsts.ViewModelMatrixName, viewMatrix);
		sp.setUniformf(GameConsts.LightPosName, lightPos);
		mesh.draw(sp);
		sp.end();
	}
	
	public void destroy() {
		boxRegion.free();
	}
	
	public void drawShadow(ShaderProgram sp, Matrix4 projViewMatrix) {
		if (currentTime > timeToLive) {
			return;
		}
		
		sp.setUniformMatrix("u_modelMatrix", modelMatrix);
		sp.setUniformMatrix(GameConsts.ProjectionMatrixName,  projViewMatrix);
		mesh.draw(sp);		
	}
	
	public Vector3 GetPosition() {
		return position;
	}
	
	public float getScale() {
		return boxType.getScale() * baseScale;
	}
	
	public int getPoints() {
		return boxType.getPoint();
	}
	
	public void applyUpgrade(ChuckObject co) {
		boxType.applyUpgrade(co);
	}	
}
