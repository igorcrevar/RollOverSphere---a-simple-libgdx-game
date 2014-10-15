package com.igorcrevar.rolloverchuck.physics;

import com.badlogic.gdx.math.Vector3;

public interface ICollisionIterationHandler {
	
	/**
	 * Call for every interpolation step
	 * @param position current position
	 * @param time between [0, 1]
	 * @param tag
	 * @return true to break iteration or false to continue
	 */
	public boolean iterationHandler(Vector3 position, float time, Object tag);
}
