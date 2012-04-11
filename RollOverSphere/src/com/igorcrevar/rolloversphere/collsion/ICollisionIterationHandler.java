package com.igorcrevar.rolloversphere.collsion;

import com.badlogic.gdx.math.Vector3;

public interface ICollisionIterationHandler {
	
	/**
	 * Call for every interpolation step
	 * @param position current position
	 * @param tag
	 * @return true to break iteration or false to continue
	 */
	public boolean iterationHandler(Vector3 position, Object tag);
}
