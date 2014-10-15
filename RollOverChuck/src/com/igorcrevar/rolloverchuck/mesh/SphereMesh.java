package com.igorcrevar.rolloverchuck.mesh;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.igorcrevar.rolloverchuck.GameData;

public class SphereMesh implements IMesh {
	private Mesh mesh;
	
	public SphereMesh(GameData gameData){
		mesh = getMesh(20, 20, gameData.Chuck_r, true);
	}

	@Override
	public void draw(ShaderProgram shaderProgram) {
		mesh.render(shaderProgram, GL20.GL_TRIANGLES);
	}

	@Override
	public void dispose() {
		mesh.dispose();
	}
	
	private Mesh getMesh(int rings, int sectors, float radius, boolean color) {
		float ringFactor = 1.0f / (rings - 1);
        float sectorFactor = 1.0f / (sectors - 1);
        
        int verticesNum = rings * sectors;
        verticesNum *= (!color ? 5 : 7);
        float vertices[] = new float[verticesNum];
        int vIndex = 0;
        for (int r = 0; r < rings; r++) { 
        	for (int s = 0; s < sectors; s++) {
        		float sf = s * sectorFactor;
        		float rf = r * ringFactor;
                float x = (float)(Math.cos(Math.PI * 2.0f * sf) * Math.sin(Math.PI * rf));
                float y = (float)Math.sin(-Math.PI / 2.0f + Math.PI * rf);
                float z = (float)(Math.sin(Math.PI * 2.0f * sf) * Math.sin(Math.PI * rf));
                
                vertices[vIndex++] = x * radius;
                vertices[vIndex++] = y * radius;
                vertices[vIndex++] = z * radius;
                if (!color) {
                	vertices[vIndex++] = s * sectorFactor;
                    vertices[vIndex++] = r * ringFactor;                     
                }
                else {
                	float factor = (sf + rf) / 2.0f * 4.0f;
                	float colorFloat = Color.toFloatBits(255, 245, (int)(factor * 40.0f) + 20, 255);
                	vertices[vIndex++] = colorFloat;
                	vertices[vIndex++] = x;
                	vertices[vIndex++] = y;
                    vertices[vIndex++] = z;
                }
        	}
        }

        int indicesNum = (rings - 1) * (sectors - 1) * 6;
        short[] indices = new short[indicesNum];
        int iIndex = 0;
        for(int r = 0; r < rings - 1; r++) {
        	for(int s = 0; s < sectors - 1; s++) {
        		int curRow = r * sectors;
        	    int nextRow = (r+1) * sectors;

        	    indices[iIndex++] = (short)(curRow + s);
        		indices[iIndex++] = (short)(nextRow + s);
        		indices[iIndex++] = (short)(nextRow + (s+1));
        		
        		indices[iIndex++] = (short)(curRow + s);
        		indices[iIndex++] = (short)(nextRow + (s+1));        		
        		indices[iIndex++] = (short)(curRow + (s+1));
        	}        	
        }
        // populate normals
        //com.igorcrevar.rolloverchuck.utils.GameHelper.populateNormals(vertices, indices, 7, 0, 4);
        // final create libgdx mesh
		Mesh newMesh = new Mesh(true, verticesNum, indicesNum, VertexAttribute.Position(), VertexAttribute.Color(), VertexAttribute.Normal());		
		newMesh.setIndices(indices);
		newMesh.setVertices(vertices);
		return newMesh;
	}
}
