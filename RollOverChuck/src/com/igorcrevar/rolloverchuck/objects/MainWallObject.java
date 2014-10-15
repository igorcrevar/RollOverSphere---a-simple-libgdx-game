package com.igorcrevar.rolloverchuck.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.igorcrevar.rolloverchuck.GameConsts;
import com.igorcrevar.rolloverchuck.GameData;
import com.igorcrevar.rolloverchuck.GameManager;
import com.igorcrevar.rolloverchuck.mesh.IMesh;

public class MainWallObject {
	private IMesh mesh;
	private ShaderProgram sp;
	private Matrix4 modelMatrix;
	private Color colorFactor = new Color(0.1f, 0.1f, 0.2f, 1.0f);
	private Vector2[] points = new Vector2[4];
	private GameData gameData;
	
	public MainWallObject(GameManager gameManager) {		
		mesh = gameManager.getMesh("cube_with_normals");
		sp = gameManager.getShader("light");
		modelMatrix = new Matrix4();
		gameData = gameManager.getGameData();
		for (int i = 0; i < 4; ++i) {
			points[i] = new Vector2();
		}
	}
	
	public void init(int x, int y, int width, int height) {		
		calc(points[0], x, y);
		calc(points[1], x + width, y);
		calc(points[2], x + width, y + height);
		calc(points[3], x, y + height);
		
		float stX = points[0].x;
		float stY = points[0].y;
		float endX = points[2].x;
		float endY = points[2].y;
		modelMatrix.idt().translate(GameData.ObjectZero);
		modelMatrix.translate((endX + stX) / 2.0f, 0.75f, (endY + stY) / 2.0f).scale(endX - stX, 1.5f, endY - stY);
	}
	
	public void draw(Matrix4 projViewMatrix, Matrix4 viewMatrix, Vector3 lightPos) {
		projViewMatrix.mul(modelMatrix);		
		viewMatrix.mul(modelMatrix);
		sp.begin();
		sp.setUniformMatrix(GameConsts.ProjectionMatrixName,  projViewMatrix);
		sp.setUniformf("u_color_factor", colorFactor);
		sp.setUniformMatrix(GameConsts.ViewModelMatrixName, viewMatrix);
		sp.setUniformf(GameConsts.LightPosName, lightPos);
		mesh.draw(sp);
		sp.end();
	}
	
	public void drawShadow(ShaderProgram sp, Matrix4 projViewMatrix) {
		sp.setUniformMatrix("u_modelMatrix", modelMatrix);
		sp.setUniformMatrix(GameConsts.ProjectionMatrixName,  projViewMatrix);
		mesh.draw(sp);		
	}
	
	private void calc(Vector2 v, int x, int y) {
		v.set(-gameData.FieldSize / 2.0f + x * gameData.CubeRegionSize,
			  -gameData.FieldSize / 2.0f + y * gameData.CubeRegionSize);
	}
}
