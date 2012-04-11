package com.igorcrevar.rolloversphere.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.igorcrevar.rolloversphere.mesh_gl10.AbstractMesh;
import com.igorcrevar.rolloversphere.mesh_gl10.BallMesh;
import com.igorcrevar.rolloversphere.mesh_gl10.MeshManager;
import com.igorcrevar.rolloversphere.texture.TextureManager;

public class ChuckSphere extends GameObject{
	public static final float DEFAULT_FRICTION = 0.85f;
	public static final float SMALL_FRICTION = 0.65f;
	public float maxZ;
	public float maxX;
	public float minX;
	public float minZ;
	
	public Vector3 movingVelocity = new Vector3(0.0f, 0.0f, 0.0f); //in seconds
	private Vector3 rotationReal = new Vector3(0.0f, 0.0f, 0.0f);
	public float friction = DEFAULT_FRICTION;
	
	public ChuckSphere(){
	}
	
	@Override
	protected Texture getTexture() {
		return TextureManager.getInstance().getTexture("badlogic");
	}

	@Override
	protected AbstractMesh getMesh() {
		BallMesh mesh = (BallMesh)MeshManager.getInstance().getMesh("ball");
		boundingSphereR = mesh.r;
		return mesh;
	}
	
	@Override
	public void update(float timeDiff) {
		//update position and rotation
		position.x += timeDiff * movingVelocity.x;
		position.z += timeDiff * movingVelocity.z;
		
		//update rotations
		rotationReal.x = fixRotation(rotationReal.x + timeDiff * movingVelocity.z * 8);
		rotationReal.z = fixRotation(rotationReal.z - timeDiff * movingVelocity.x * 8);
		
		//gimbal lock solver - just pick "more important" rotation on every frame
 		if (Math.abs(movingVelocity.z) >= Math.abs(movingVelocity.x)){
			rotation.x = rotationReal.x;
			rotation.z = 0.0f;
		}
		else{
			rotation.x = 0.0f;
			rotation.z = rotationReal.z;
		}
		
		//update velocities
		movingVelocity.x -= movingVelocity.x * friction * timeDiff;
		movingVelocity.z -= movingVelocity.z * friction * timeDiff;		
	
		//handle position boundaries
		if (position.x < minX){
			position.x = minX;
			movingVelocity.x = -movingVelocity.x;
		}
		
		if (position.x > maxX){
			position.x = maxX;
			movingVelocity.x = -movingVelocity.x;
		}
		
		if (position.z < minZ){
			position.z = minZ;
			movingVelocity.z = -movingVelocity.z;
		}
		
		if (position.z > maxZ){
			position.z = maxZ;
			movingVelocity.z = -movingVelocity.z;
		}
	}
	
	private float fixRotation(float value){
		if (value > 360.0f){
			return 360.0f - value;
		}
		else if (value < 0){
			return value + 360.0f;
		}
		
		return value;
	}

}
