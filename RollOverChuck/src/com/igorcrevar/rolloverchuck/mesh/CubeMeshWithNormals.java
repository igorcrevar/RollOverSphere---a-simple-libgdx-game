package com.igorcrevar.rolloverchuck.mesh;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.igorcrevar.rolloverchuck.GameData;

public class CubeMeshWithNormals implements IMesh {
private Mesh mesh;
	
	public CubeMeshWithNormals(GameData gameData) {
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
		short[] indices = new short[36];
		int index = 0;
		for (int i = 0; i < 6; ++i) {
			indices[index++] = (short)(i * 4);
			indices[index++] = (short)(i * 4 + 1);
			indices[index++] = (short)(i * 4 + 3);
			indices[index++] = (short)(i * 4 + 0);
			indices[index++] = (short)(i * 4 + 3);
			indices[index++] = (short)(i * 4 + 2);
		}
	
		float color = Color.toFloatBits(255, 255, 255, 255);
		float[] vertices = new float[] {
			-size, -size,  size, color, 0.0f, 0.0f, 1.0f,
             size, -size,  size, color, 0.0f, 0.0f, 1.0f,
            -size,  size,  size, color, 0.0f, 0.0f, 1.0f,
             size,  size,  size, color, 0.0f, 0.0f, 1.0f,

             size, -size,  size, color, 1.0f, 0.0f, 0.0f,
             size, -size, -size, color, 1.0f, 0.0f, 0.0f,        
             size,  size,  size, color, 1.0f, 0.0f, 0.0f,
             size,  size, -size, color, 1.0f, 0.0f, 0.0f,

             size, -size, -size, color, 0.0f, 0.0f, -1.0f,
            -size, -size, -size, color, 0.0f, 0.0f, -1.0f,           
             size,  size, -size, color, 0.0f, 0.0f, -1.0f,
            -size,  size, -size, color, 0.0f, 0.0f, -1.0f,

            -size, -size, -size, color, -1.0f, 0.0f, 0.0f,
            -size, -size,  size, color, -1.0f, 0.0f, 0.0f,        
            -size,  size, -size, color, -1.0f, 0.0f, 0.0f,
            -size,  size,  size, color, -1.0f, 0.0f, 0.0f,

            -size, -size, -size, color, 0.0f, -1.0f, 0.0f,
             size, -size, -size, color, 0.0f, -1.0f, 0.0f,        
            -size, -size,  size, color, 0.0f, -1.0f, 0.0f,
             size, -size,  size, color, 0.0f, -1.0f, 0.0f,

            -size,  size,  size, color, 0.0f, 1.0f, 0.0f,
             size,  size,  size, color, 0.0f, 1.0f, 0.0f,          
            -size,  size, -size, color, 0.0f, 1.0f, 0.0f,
             size,  size, -size, color, 0.0f, 1.0f, 0.0f,};
     
		// populate normal also
        Mesh newMesh = new Mesh(true, vertices.length / 7, indices.length, VertexAttribute.Position(), VertexAttribute.Color(), VertexAttribute.Normal());
        newMesh.setVertices(vertices);
        newMesh.setIndices(indices);
        return newMesh;
	}
}
