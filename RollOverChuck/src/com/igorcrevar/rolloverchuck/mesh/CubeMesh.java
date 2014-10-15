package com.igorcrevar.rolloverchuck.mesh;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.igorcrevar.rolloverchuck.GameData;

public class CubeMesh implements IMesh {
	private Mesh mesh;
	
	public CubeMesh(GameData gameData) {
		mesh = getMesh(0.5f);
	}
	
	@Override
	public void draw(ShaderProgram shaderProgram) {
		mesh.render(shaderProgram, GL20.GL_TRIANGLES);
	}

	@Override
	public void dispose() {
		mesh.dispose();
	}

	private Mesh getMesh(float size) {
		short[] indices = new short[] { 
			// Faces definition
			0, 1, 3, 0, 3, 2, 		// Face front
			4, 5, 7, 4, 7, 6, 		// Face right
			8, 9, 11, 8, 11, 10, 	// ...
			12, 13, 15, 12, 15, 14, 
			16, 17, 19, 16, 19, 18, 
			20, 21, 23, 20, 23, 22, 
		};
				
		float[] vertices = new float[] {
			-size, -size, size, 0.0f, 0.0f,
			size, -size, size, 0.0f, 1.0f, 
			-size, size, size, 1.0f, 0.0f, 
			size, size, size, 1.0f, 1.0f,

			size, -size, size, 0.0f, 0.0f,
			size, -size, -size, 0.0f, 1.0f,  
			size, size, size, 1.0f, 0.0f,
			size, size, -size, 1.0f, 1.0f,

			size, -size, -size, 0.0f, 0.0f,
			-size, -size, -size, 0.0f, 1.0f,
			size, size, -size, 1.0f, 0.0f, 
			-size, size, -size, 1.0f, 1.0f,

			-size, -size, -size, 0.0f, 0.0f, 
			-size, -size, size, 0.0f, 1.0f, 
			-size, size, -size, 1.0f, 0.0f,
			-size, size, size, 1.0f, 1.0f,

			-size, -size, -size, 0.0f, 0.0f, 
			size, -size, -size, 0.0f, 1.0f,
			-size, -size, size, 1.0f, 0.0f,
			size, -size, size, 1.0f, 1.0f,

			-size, size, size, 0.0f, 0.0f,
			size, size, size, 0.0f, 1.0f,
			-size, size, -size, 1.0f, 0.0f, 
			size, size, -size, 1.0f, 1.0f,
		};   
     
		// populate normal also
        Mesh newMesh = new Mesh(true, vertices.length / 5, indices.length, VertexAttribute.Position(), VertexAttribute.TexCoords(0));
        newMesh.setIndices(indices);
        newMesh.setVertices(vertices);
        return newMesh;
	}
}
