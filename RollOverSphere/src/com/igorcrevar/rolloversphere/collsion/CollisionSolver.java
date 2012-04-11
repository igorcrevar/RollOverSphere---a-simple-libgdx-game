package com.igorcrevar.rolloversphere.collsion;

import com.badlogic.gdx.math.Vector3;

public class CollisionSolver {
	private static final float ZERO = 0.00001f;
	//HOW MUCH WE CHANGE indipendant value in EVERY ITERATION
	private static float step = 0.5f;

	/**
	 * Set step for every iteration of interpolation
	 * @param s
	 */
	public void setStep(float s){
		step = s;
	}
	/**
	 * Check if two spheres collides
	 * @param p1 pos of first sphere
	 * @param r1 r of first sphere
	 * @param p2 position of second sphere
	 * @param r2 r of second
	 * @return true if collide occured
	 */
	public static boolean isCollide(Vector3 p1, float r1, Vector3 p2, float r2){
		float dist = p1.dst(p2);
		
		return r1 + r2 > dist;
	}
	
	public static boolean isZero(float v){
		return Math.abs(v) < ZERO;
	}
	
	/**
	 * Returns -1 if v < 0 , 0 if v == 0 and 1 if v > 0 
	 * @param v
	 * @return
	 */
	public static int getSign(float v){
		if (v < 0.0f){
			return -1;
		}		
		if (v > 0.0f){
			return 1;
		}
		
		return 0;
	}
	
	private static int getNumberOfSteps(float diff, float step){
		float stepsHelper = Math.abs(diff) / step; 
		//round on greater number
		if (isZero(stepsHelper - (float)Math.floor(stepsHelper))){
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
	public static void iterateOverX(ICollisionIterationHandler handler, Object tag, Vector3 start, Vector3 end){
		float diff = end.x - start.x;
		int steps = getNumberOfSteps(diff, step);
		float dx = getSign(diff) * step;
		float dy = (end.y - start.y) / steps;
		float dz = (end.z - start.z) / steps;
		Vector3 position = new Vector3(start); //position is copy of start
		
		for (int i = 0; i < steps; ++i){			
			if (!handler.iterationHandler(position, tag)){ //callback
				return;
			}
			position.x += dx;
			position.y += dy;
			position.z += dz;
		}
		//solve once more for end position
		position.set(end);
		handler.iterationHandler(position, tag); //callback
	}
	
	/**
	 * Interpolate over Z and call handler for every iteration. z2 must not be equal to z1!!!
	 * @param handler
	 * @param tag
	 * @param start
	 * @param end
	 */
	public static void iterateOverZ(ICollisionIterationHandler handler, Object tag, Vector3 start, Vector3 end){
		float diff = end.z - start.z;
		int steps = getNumberOfSteps(diff, step);	
		float dx = (end.x - start.x) / steps;
		float dy = (end.y - start.y) / steps;
		float dz = getSign(diff) * step;
		Vector3 position = new Vector3(start); //position is copy of start
		
		for (int i = 0; i < steps; ++i){			
			if (!handler.iterationHandler(position, tag)){ //callback
				return;
			}
			position.x += dx;
			position.y += dy;
			position.z += dz;
		}
		//solve once more for end position
		position.set(end);
		handler.iterationHandler(position, tag); //callback
	}
	
	/**
	 * Iterate from posStart to posEnd and pass control on every iteration to handler
	 * @param handler
	 * @param posStart
	 * @param posEnd
	 * @param will be passed to handler
	 */
	public static void iterateOver(ICollisionIterationHandler handler, Vector3 posStart, Vector3 posEnd, Object tag){
		float diffx = Math.abs(posEnd.x - posStart.x);
		float diffz = Math.abs(posEnd.z - posStart.z);
		
		if (diffx > diffz){
			iterateOverX(handler, tag, posStart, posEnd);
		}
		else if (isZero(diffz)){
			handler.iterationHandler(new Vector3(posStart), tag); //callback
		}
		else {
			iterateOverZ(handler, tag, posStart, posEnd);
		}
	}
}
