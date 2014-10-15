package com.igorcrevar.rolloverchuck.objects.boxes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.igorcrevar.rolloverchuck.GameManager;
import com.igorcrevar.rolloverchuck.objects.BoxObject;

public class BoxManager {
	private static final int MAX_COUNT = 50;
	private BoxObject[] boxes = new BoxObject[MAX_COUNT];
	private Matrix4 tmp1 = new Matrix4();
	private Matrix4 tmp2 = new Matrix4();
	private int numberActive;
	
	public BoxManager(GameManager gameManager) {
		numberActive = 0;
		for (int i = 0; i < MAX_COUNT; ++i) {
			boxes[i] = new BoxObject(gameManager);
		}
	}
	
	public void init() {
		for (int i = 0; i < numberActive; ++i) {
			BoxObject box = boxes[i];
			box.destroy();
		}
		
		numberActive = 0;
	}
	
	public void update(float deltaTime) {
		int start = numberActive - 1;
		for (int i = start; i >= 0; --i) {
			BoxObject box = boxes[i];
			if (!box.update(deltaTime)) {
				remove(i);	
			}
		}
	}
	
	public void draw(Matrix4 projViewMatrix, Matrix4 viewMatrix, Vector3 lightPos) {
		for (int i = numberActive - 1; i >= 0; --i) {
			tmp1.set(projViewMatrix);
			tmp2.set(viewMatrix);
			boxes[i].draw(tmp1, tmp2, lightPos);
		}
	}
	
	public void drawShadow(ShaderProgram sp, Matrix4 projViewMatrix) {
		for (int i = numberActive - 1; i >= 0; --i) {
			tmp1.set(projViewMatrix);
			boxes[i].drawShadow(sp, tmp1);
		}
	}
	
	public void remove(int i) {
		// free resources / etc for box on position i
		boxes[i].destroy();
		// decrement number of active
		--numberActive;
		// put last active on i-th position
		BoxObject tmp = boxes[i];
		boxes[i] = boxes[numberActive];
		boxes[numberActive] = tmp;
	}
	
	public Vector3 add(IBoxRegion boxRegion, float timeToLive, IBoxType boxType) {
		if (!boxRegion.isFree()) {
			// throw new IllegalArgumentException("Box region passed to add BoxManager is not available! This should not happen");
			Gdx.app.log("chuck", "BoxManager region is not free"); 
			return null;
		}
		
		// no space for another
		if (numberActive == MAX_COUNT) {
			Gdx.app.log("chuck", "BoxManager couldn't find a element in poll");
			return null;
		}

		BoxObject newPP = boxes[numberActive++];
		newPP.init(boxRegion, timeToLive, boxType);
		return newPP.GetPosition();
	}
	
	public BoxObject getBoxObject(int i) {
		return i >= 0 && i < MAX_COUNT ? boxes[i] : null;
	}
	
	public int getAvailableCount() {
		return numberActive;
	}
}
