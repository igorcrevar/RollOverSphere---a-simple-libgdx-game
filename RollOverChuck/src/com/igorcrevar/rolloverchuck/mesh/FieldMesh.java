package com.igorcrevar.rolloverchuck.mesh;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.igorcrevar.rolloverchuck.GameData;

public class FieldMesh implements IMesh {
	private Mesh mesh;
	private float[] vertices;
	
	public FieldMesh(GameData gameData) {
		float half = gameData.FieldSize / 2.0f;
		half += gameData.Chuck_r;
		mesh = new Mesh(true, 4, 0, VertexAttribute.Position(), VertexAttribute.TexCoords(0));
		vertices = new float[] {
			-half, 0.0f, -half, 0.0f, 0.0f,
            -half, 0.0f,  half, 0.0f, 1.0f,
             half, 0.0f, -half, 1.0f, 0.0f,
             half, 0.0f,  half, 1.0f, 1.0f	
		};
		mesh.setVertices(vertices);
	}
	
	@Override
	public void draw(ShaderProgram shader) {
		mesh.render(shader, GL20.GL_TRIANGLE_STRIP, 0, 4);
	}

	@Override
	public void dispose() {
		mesh.dispose();
	}
}
