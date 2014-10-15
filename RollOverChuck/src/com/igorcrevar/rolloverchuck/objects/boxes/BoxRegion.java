package com.igorcrevar.rolloverchuck.objects.boxes;

import com.badlogic.gdx.math.Vector3;


/**
 * @author crewce
 * At one time only one box can exist in one region
 * This is mandatory and will not change
 */
public class BoxRegion implements IBoxRegion {
	protected float posX;
	protected float posY;
	protected float cellCenterOffset;
	protected boolean isFree;

	public BoxRegion(float posX, float posY, float cellCenterOffset) {
		this.posX = posX;
		this.posY = posY;
		this.cellCenterOffset = cellCenterOffset;
		this.isFree = true;
	}
	
	@Override
	public void take() {
		isFree = false;
	}

	@Override
	public void free() {
		isFree = true;
	}

	@Override
	public boolean isFreeAndNotNear(Vector3 basePosition, Vector3 tmpVector,
			float minDistanceSquared) {
		if (!isFree) {
			return false;
		}
		
		populatePosition(tmpVector);
		return tmpVector.dst2(basePosition) >= minDistanceSquared;
	}

	@Override
	public boolean isFree() {
		return isFree;
	}

	@Override
	public void populatePosition(Vector3 pos) {
		pos.set(posX + cellCenterOffset, 0.0f, posY + cellCenterOffset);
	}
}
