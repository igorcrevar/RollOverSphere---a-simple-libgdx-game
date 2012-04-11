package com.igorcrevar.rolloversphere.mesh_gl10;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class WaterMesh extends AbstractMesh{
	private int mNumVertices;
	protected int mXVertexCount = 20;
	protected int mYVertexCount = 20;
	protected float mMaxX = 5.40f;
	protected float mMaxY = 5.40f;

	protected float mAngle = 0;
	 
	public WaterMesh() {

	}
	
	@Override
	public Mesh createMesh() {
		int ind, i, j;
		
		int numVertices = mYVertexCount * mXVertexCount;
		int numIndices = (mXVertexCount-1) * (mYVertexCount-1) * 6;
		
		float[] vertices = new float[numVertices * 5];
		short[] indices = new short[numIndices];

		// init vertex
		ind = 0;
		for (i = 0; i < mYVertexCount; ++i) {
			for (j = 0; j < mXVertexCount; ++j) {
				vertices[ind++] = -mMaxX + mMaxX * 2 * j / mXVertexCount; //(-XVertexCount + j * 2) * 0.5f; // x
				vertices[ind++] = 0; // y
				vertices[ind++] = -mMaxY + mMaxY * 2 * i / mYVertexCount;//(-YVertexCount + i * 2) * 0.5f; // z
				
				vertices[ind++] = j > 0 ? (float)(j + 1) / mXVertexCount : 0.0f;
				vertices[ind++] = i > 0 ? (float)(i + 1) / mYVertexCount : 0.0f;
			}
		}

		int indexIndicies = 0;
		int rowStartVertexInd = 0;
		for (i = 0; i < mYVertexCount - 1; ++i) {
			for (j = 0; j < mXVertexCount - 1; ++j) {
				indices[indexIndicies++] = (short) (rowStartVertexInd + j);
				indices[indexIndicies++] = (short) (rowStartVertexInd + j + 1);
				indices[indexIndicies++] = (short) (rowStartVertexInd + j + mXVertexCount);
				
				indices[indexIndicies++] = (short) (rowStartVertexInd + j + 1);
				indices[indexIndicies++] = (short) (rowStartVertexInd + j + mXVertexCount + 1);
				indices[indexIndicies++] = (short) (rowStartVertexInd + j + mXVertexCount);
			}
			rowStartVertexInd += mXVertexCount;
		}

		mMesh = new Mesh(false, numVertices, numIndices, 
				new VertexAttribute(Usage.Position, 3, "aPosition"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "aTexCoords"));

		
		mMesh.setIndices(indices);
		mMesh.setVertices(vertices);
		mNumVertices = numVertices;
		return mMesh;
	}

	@Override
	public void init(Object... values) {
		if (values.length > 0){
			mXVertexCount = (Integer)values[0];
			mYVertexCount = (Integer)values[1];
			mMaxX = (Float)values[2];
			mMaxY = (Float)values[3];
		}
	}
	
	public void updateMesh(float timeDiff) {
		float[] verticesBuffer = new float[mNumVertices * 5];
		mMesh.getVertices(verticesBuffer);
		
		int offset = 0;      
		for (int i = 0; i < mYVertexCount; ++i){ 
			float tmpAngle = mAngle;
			float incY = (float)Math.cos( ( mAngle + 40.0f * (mYVertexCount-i) ) / 180 * 3.14);
        	for (int j = 0; j < mXVertexCount; ++j) {
				float newY = (float)Math.sin(tmpAngle / 180 * 3.14) * incY * 2.5f;
				verticesBuffer[offset + 1] = newY;
				tmpAngle += 90 / mXVertexCount;
				offset += 5;
			} 
        	
			tmpAngle += 10;
		}
		
		mAngle += 5;
        if (mAngle > 360.0f)
        {
        	mAngle = 360.0f - mAngle;
        }
        mMesh.setVertices(verticesBuffer);
 	}
}

