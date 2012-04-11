package com.igorcrevar.rolloversphere.mesh_gl10;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class CubeMesh extends AbstractMesh{
	public float size = 1.0f;
	public float color;
	
	@Override
	public void init(Object... values) {
		if (values.length > 0){
			size = (Float)values[0];			
			if (values.length > 1){
				color = (Float)values[1];
			}
		}
	}

	@Override
	public Mesh createMesh() {
		short[] vertices = new short[] { 
				0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4,
				0, 4, 7, 7, 3, 0, 1, 5, 6, 6, 2, 1,
				0, 1, 5, 5, 4, 0, 3, 2, 6, 6, 7, 3
		};
		Mesh mesh = new Mesh(true, 8, vertices.length, 
                 new VertexAttribute(Usage.Position, 3, "a_position"));
		 //new VertexAttribute(Usage.ColorPacked, 4, "a_color"));
		 //Color.toFloatBits(96, 0, 0, 255),

		mesh.setVertices(new float[] {
                 -size, size, -size,
                 size,  size, -size,
                 size, -size, -size, 
                 -size, -size, -size,

                 -size, size, size,
                 size,  size, size,
                 size, -size, size, 
                 -size, -size, size,
                });   
         mesh.setIndices(vertices);
         mMesh = mesh;
         return mesh;
	}

}
