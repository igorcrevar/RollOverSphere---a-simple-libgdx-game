package com.igorcrevar.rolloversphere.mesh_gl10;

import java.nio.FloatBuffer;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector3;

/**
 * @author igor.crevar
 *
 */
public abstract class AbstractMesh {
	protected Mesh mMesh;
	
	public abstract void init(Object... values);
	public abstract Mesh createMesh();
	
	public FloatBuffer getVertexBuffer() {		
		return mMesh.getVerticesBuffer();
	}

	public Mesh getMesh() {
		return mMesh;
	}

	public void dispose() {
		try{
			mMesh.dispose();
		}
		catch(Exception e){
			System.out.println(mMesh + e.getMessage());
		}
	}
	
	//override in childs if needed
	public int getRenderTechnique()
	{
		return GL10.GL_TRIANGLES;
	}

	
	/**
	 * Returns triangle normal
	 * @param v1 point of triangle
	 * @param v2 point of triangle
	 * @param v3 point of triangle
	 * @return normal to triangle
	 */
	protected Vector3 Triangle3DCalculateSurfaceNormal(Vector3 v1, Vector3 v2, Vector3 v3)
	{
		Vector3 u = v2.sub(v1);
		Vector3 v = v3.sub(v1);
	    
	    Vector3 ret = new Vector3();
	    ret.x = (u.y * v.z) - (u.z * v.y);
	    ret.y = (u.z * v.x) - (u.x * v.z);
	    ret.z = (u.x * v.y) - (u.y * v.x);
	    return ret;
	}
}
