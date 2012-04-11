package com.igorcrevar.rolloversphere.mesh_gl10;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class PlaneXZMesh extends AbstractMesh{

	public float sizeX = 1.0f;
	public float sizeZ = 1.0f;
	public float color;
	
	@Override
	public void init(Object... values) {
		if (values.length > 0){
			sizeX = (Float)values[0];			
			if (values.length > 1){
				sizeZ = (Float)values[1];
				if (values.length > 2){
					color = (Float)values[2];
				}
			}
		}
	}

	@Override
	//use with triangle FAN!
	public Mesh createMesh() {		
		Mesh mesh = new Mesh(true, 6, 0, 
                 new VertexAttribute(Usage.Position, 3, "a_position"));
		
		mesh.setVertices(new float[] {
				 0, 0, 0, 
                 -sizeX, 0, -sizeZ,
                 sizeX,  0, -sizeZ,
                 sizeX, 0, sizeZ, 
                 -sizeX, 0, sizeZ,
                 -sizeX, 0, -sizeZ });           
         mMesh = mesh;
         return mesh;
	}
	
	@Override
	public int getRenderTechnique()
	{
		return GL10.GL_TRIANGLE_FAN;
	}

}
