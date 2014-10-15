package com.igorcrevar.rolloverchuck.physics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class SphereMoving {
	private Vector3 position = new Vector3();
	private Vector3 velocity = new Vector3();
	private Vector3 rotationAxis = new Vector3();
	private float angle;
	
	private Vector3 friction = new Vector3();
	private Vector3 maxVelocity = new Vector3();
	private Vector3 boundary = new Vector3();
	private Vector3 bouncingFactor = new Vector3();
	
	public void init(Vector3 boundary, Vector3 friction, Vector3 maxVelocity, Vector3 bouncingFactor) {
		position.set(0f, 0f, 0f);
		velocity.set(0f, 0f, 0f);
		rotationAxis.set(0f, 0f, 0f);
		setFriction(friction);
		setMaxVelocity(maxVelocity);
		setBoundary(boundary);
		setBouncingFactor(bouncingFactor);
		angle = 0;
	}
	
	public void setBouncingFactor(Vector3 bouncingFactor) {
		this.bouncingFactor.set(bouncingFactor);
	}

	public void setMaxVelocity(Vector3 maxVelocity) {
		this.maxVelocity.set(maxVelocity);
	}
	
	public void setFriction(Vector3 friction) {
		this.friction.set(friction);
	}
	
	public void setBoundary(Vector3 boundary) {
		this.boundary.set(boundary);
	}
	
	public Vector3 getPosition() {
		return position;
	}
	
	public void populateMatrix(Matrix4 m, float scaleFactor) {
		m.translate(position).scale(scaleFactor, scaleFactor, scaleFactor).rotate(rotationAxis, angle);
	}
	
	public void reflect(boolean rX, boolean rY) {
		if (rX) {
			velocity.x = -velocity.x;
		}
		if (rY) {
			velocity.y = -velocity.y;
		}
	}
	
	public void addVelocity(float incX, float incZ) {
		float dirX = Math.signum(velocity.x);
		float dirZ = Math.signum(velocity.z);
		float dxSign = Math.signum(incX);
		float dzSign = Math.signum(incZ);
		
		// direction change - zero
		if (Math.abs(dxSign - dirX) == 2) {
			velocity.x = 0;
		}
		
		velocity.x += incX;
		// check if MAX X reached
		if (Math.abs(velocity.x) > maxVelocity.x) {
			velocity.x = Math.signum(velocity.x) * maxVelocity.x; 
		}
		
		// direction change - zero
		if (Math.abs(dzSign - dirZ) == 2) {
			velocity.z = 0.0f;
		}
		
		velocity.z += incZ;
		// check if MAX Z reached
		if (Math.abs(velocity.z) > maxVelocity.z) {
			velocity.z = Math.signum(velocity.z) * maxVelocity.z; 
		}
		
		changeDirection();
	}
	
	public void update(float deltaTime) {
		//update position and rotation
		position.x += deltaTime * velocity.x;
		position.z += deltaTime * velocity.z;
	
		// angle update - increment depends on intensity of velocity
		float velInt = velocity.set(velocity).len2();
		angle = fixAngle(angle + velInt * deltaTime * 5.0f);
				
		// update velocity X
		float dir = Math.signum(velocity.x);
		velocity.x -= deltaTime * friction.x * dir;
		// if sign of velocity is changed then we are on zero
		if (Math.signum(velocity.x) != dir) {
			velocity.x = 0.0f;
		}
		
		// update velocity Z
		dir = Math.signum(velocity.z);
		velocity.z -= deltaTime * friction.z * dir;		
		// if sign of velocity is changed then we are on zero
		if (Math.signum(velocity.z) != dir) {
			velocity.z = 0.0f;
		}
		
		// check if boundaries reched so we change direction
		boolean changeVelocity = false;
		float angleFix = 0.0f;
		// X boundaries
		if (position.x < -boundary.x){
			position.x = -boundary.x;
			velocity.x = -velocity.x * bouncingFactor.x;
			changeVelocity = true;
			angleFix = 180.0f;
		}
		
		if (position.x > boundary.x){
			position.x = boundary.x;
			velocity.x = -velocity.x * bouncingFactor.x;
			changeVelocity = true;
			angleFix = -180.0f;
		}
		
		// Z boundaries
		if (position.z < -boundary.z){
			position.z = -boundary.z;
			velocity.z = -velocity.z * bouncingFactor.z;
			changeVelocity = true;
			angleFix = 180.0f;
		}
		
		if (position.z > boundary.z){
			position.z = boundary.z;
			velocity.z = -velocity.z * bouncingFactor.z;
			changeVelocity = true;
			angleFix = -180.0f;
		}
		
		// change direction if bouncing occured
		if (changeVelocity) {
			changeDirection();
			angle = fixAngle(angle + angleFix);
		}
	}
	
	private void changeDirection() {
		rotationAxis.set(0.0f, 1.0f, 0.0f);
		rotationAxis.crs(new Vector3(velocity).nor());
	}
	
	/**
	 * Fix rotation angle so it is inside 0..360
	 * @param value angle
	 * @return fixed angle
	 */
	private float fixAngle(float value){
		if (value > 360.0f){
			return 360.0f - value;
		}
		else if (value < 0){
			return value + 360.0f;
		}
		
		return value;
	}
}
