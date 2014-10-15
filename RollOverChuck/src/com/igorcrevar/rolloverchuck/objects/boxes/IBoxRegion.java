package com.igorcrevar.rolloverchuck.objects.boxes;

import com.badlogic.gdx.math.Vector3;

public interface IBoxRegion {
	public void free();
	/**
	 * Return true if region is available and if distance ^ 2 from position is greater minDistanceSquared
	 * @param pos position from which distance is calculated
	 * @param minDistance 
	 * @return true if available
	 */
	public boolean isFreeAndNotNear(Vector3 basePosition, Vector3 tmpVector, float minDistanceSquared);
	public boolean isFree();
	public void populatePosition(Vector3 pos);
	public void take();
}
