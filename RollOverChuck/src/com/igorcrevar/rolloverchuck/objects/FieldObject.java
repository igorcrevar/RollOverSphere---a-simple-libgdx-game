package com.igorcrevar.rolloverchuck.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.igorcrevar.rolloverchuck.GameConsts;
import com.igorcrevar.rolloverchuck.GameData;
import com.igorcrevar.rolloverchuck.GameManager;
import com.igorcrevar.rolloverchuck.mesh.IMesh;

public class FieldObject {
	private IMesh mesh;
	private ShaderProgram sp;
	private Matrix4 modelMatrix;
	private TextureRegion texture;
	private Vector2 repeatFactor;
	private Vector2 uvMin = new Vector2();
	private Vector2 uvMax = new Vector2();
	
	public FieldObject(GameManager gameManager) {		
		mesh = gameManager.getMesh("plane");
		sp = gameManager.getShader("field");
		modelMatrix = new Matrix4().translate(GameData.ObjectZero);
		// how much texture is repeated in x and z 
		float facX = (int)((Gdx.graphics.getWidth() / 1280.0f) * 8.0f);
		float facY = (int)((Gdx.graphics.getHeight() / 720.0f) * 8.0f);
		repeatFactor = new Vector2(facX, facY);
	}
	
	public void setTexture(TextureRegion textRegion) {
		texture = textRegion;
		// ((FieldMesh)mesh).setTextureUV(texture.getU(), texture.getV(), texture.getU2(), texture.getV2());
		uvMin.set(texture.getU(), texture.getV());
		uvMax.set(texture.getU2(), texture.getV2());
	}
	
	public void draw(Matrix4 viewProjMatrix) {
		viewProjMatrix.mul(modelMatrix);
		if (texture != null) {
			texture.getTexture().bind(); // bind texture
		}
		sp.begin();
		sp.setUniformMatrix(GameConsts.ProjectionMatrixName,  viewProjMatrix);
		sp.setUniformi(GameConsts.TextureName, 0);
		sp.setUniformf("u_repeat", repeatFactor);
		sp.setUniformf("u_uvMax", uvMax);
		sp.setUniformf("u_uvMin", uvMin);
		mesh.draw(sp);
		sp.end();
	}
}
