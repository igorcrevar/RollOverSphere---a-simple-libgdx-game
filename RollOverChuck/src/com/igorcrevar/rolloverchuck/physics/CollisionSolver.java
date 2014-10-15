package com.igorcrevar.rolloverchuck.physics;

import com.badlogic.gdx.math.Vector3;

public class CollisionSolver {
	private static final float ZERO = 0.00001f;
	//HOW MUCH WE CHANGE indipendant value in EVERY ITERATION
	private float step = 0.1f;
	private Vector3 tmp = new Vector3();

	/**
	 * Set step for every iteration of interpolation
	 * @param s
	 */
	public void setStep(float s) {
		step = s;
	}

	private boolean isZero(float v) {
		return Math.abs(v) < ZERO;
	}
	
	private int getNumberOfSteps(float diff, float step) {
		float stepsHelper = Math.abs(diff) / step; 
		//round on greater number
		if (isZero(stepsHelper - (float)Math.floor(stepsHelper))) {
			return (int)stepsHelper;
		}	
		
		return (int)stepsHelper + 1;
	}
	
	/**
	 * Interpolate over X and call handler for every iteration. Must not x2 == x1!!!
	 * @param handler
	 * @param tag
	 * @param start
	 * @param end
	 */
	private void iterateOverX(ICollisionIterationHandler handler, Object tag, Vector3 start, Vector3 end){
		float diff = end.x - start.x;
		int steps = getNumberOfSteps(diff, step);
		float dx = Math.signum(diff) * step;
		float dy = (end.y - start.y) / steps;
		float dz = (end.z - start.z) / steps;
		// use clone
		Vector3 position = tmp;
		position.set(start);
		
		for (int i = 0; i < steps; ++i) {			
			if (handler.iterationHandler(position, (float)i / steps, tag)){ //callback
				return;
			}
			position.x += dx;
			position.y += dy;
			position.z += dz;
		}
		//solve once more for end position
		position.set(end);
		handler.iterationHandler(position, 1.0f, tag); //callback
	}
	
	/**
	 * Interpolate over Z and call handler for every iteration. z2 must not be equal to z1!!!
	 * @param handler
	 * @param tag
	 * @param start
	 * @param end
	 */
	private void iterateOverZ(ICollisionIterationHandler handler, Object tag, Vector3 start, Vector3 end){
		float diff = end.z - start.z;
		int steps = getNumberOfSteps(diff, step);	
		float dx = (end.x - start.x) / steps;
		float dy = (end.y - start.y) / steps;
		float dz = Math.signum(diff) * step;
		// use clone
		Vector3 position = tmp;
		position.set(start);
		
		for (int i = 0; i < steps; ++i) {			
			if (handler.iterationHandler(position, (float)i / steps, tag)){ //callback
				return;
			}
			position.x += dx;
			position.y += dy;
			position.z += dz;
		}
		//solve once more for end position
		position.set(end);
		handler.iterationHandler(position, 1.0f, tag); //callback
	}
	
	/**
	 * Iterate from posStart to posEnd and pass control on every iteration to handler
	 * @param handler
	 * @param posStart
	 * @param posEnd
	 * @param will be passed to handler
	 */
	public void iterateOver(ICollisionIterationHandler handler, Vector3 posStart, Vector3 posEnd, Object tag){
		float diffx = Math.abs(posEnd.x - posStart.x);
		float diffz = Math.abs(posEnd.z - posStart.z);
		
		if (diffx > diffz) {
			iterateOverX(handler, tag, posStart, posEnd);
		}
		else if (isZero(diffz)) { // didnt move at all
			tmp.set(posStart);
			handler.iterationHandler(tmp, 1.0f, tag); //callback
		}
		else {
			iterateOverZ(handler, tag, posStart, posEnd);
		}
	}
}
