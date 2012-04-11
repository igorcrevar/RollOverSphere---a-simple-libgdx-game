package com.igorcrevar.rolloversphere.mesh_gl10;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class BallMesh extends AbstractMesh{
	public int dotsPerCircleNum;
	public float r;
	
	public BallMesh(){
	}

	protected short getIndex(int current, int start, int max)
	{
		int rv = 0;
		rv = current - start < max ? current : start;
		return (short)rv;
	}
	
	protected void addVertice(float[] vertices, float x, float y, float z, int startPosition, int offset, boolean isLast)
	{
		int vInd = (startPosition + offset) * 5;
		vertices[vInd] = x;
		vertices[vInd + 1] = y;
		vertices[vInd + 2] = z;
		vertices[vInd + 3] = !isLast ? 1.0f - (offset + 0.0f) / dotsPerCircleNum : 0.0f; 
		vertices[vInd + 4] = (startPosition / dotsPerCircleNum + 1.0f) / (dotsPerCircleNum / 2 + 1.0f);	
	}
	
	@Override
	public void init(Object... values) {
		if (values.length > 0){
			this.r = (Float)values[0];
			if (values.length > 1){
				this.dotsPerCircleNum = (Integer)values[1];
			}
		}
	}
	
	@Override
	public Mesh createMesh() {
		float sin, cos, y, x, z, currentR;
		int numVertices = (dotsPerCircleNum / 2 + 1) * (dotsPerCircleNum + 1);//+2 two center points
        int numIndices = (dotsPerCircleNum / 2) * dotsPerCircleNum * 2; //only vertical - need to calculate horiz +DOTS

        numIndices *= 3;
        numVertices *= 5;
        float[] vertices = new float[numVertices];
        short[] indices = new short[numIndices]; 
        
        int vPositionUp = (dotsPerCircleNum / 4) * (dotsPerCircleNum+1);
        int vPositionDown = (dotsPerCircleNum / 4) * (dotsPerCircleNum+1);
      
        int indI = 0;
        
        float mainAngle = 0;
        float step = 360.0f / (dotsPerCircleNum);
        
        int yDots = dotsPerCircleNum / 4;
        
        for (int j = 0; j <= yDots; ++j)
        {
        	sin = (float)Math.sin(mainAngle / 180 * Math.PI);
        	cos = (float)Math.cos(mainAngle / 180 * Math.PI);
        	y = sin * r;
        	currentR = cos * r;
        	float angle = 0;
			for (int i = 0; i < dotsPerCircleNum; ++i) {
				sin = (float) Math.sin(angle / 180 * Math.PI);
				cos = (float) Math.cos(angle / 180 * Math.PI);
				x = -currentR * cos;
				z = currentR * sin;
				
				addVertice(vertices, x, y, z, vPositionUp, i, false);
				if (j > 0) {
					addVertice(vertices, x, -y, z, vPositionDown, i, false);
						
					//set indices
					indices[indI++] = (short)(vPositionDown - dotsPerCircleNum - 1 +  i);
					indices[indI++] = (short)(vPositionDown - dotsPerCircleNum - 1 + (i+1));
					indices[indI++] = (short)(vPositionDown + i);
					
					indices[indI++] = (short)(vPositionDown - dotsPerCircleNum -1 + (i+1));
					indices[indI++] = (short)(vPositionDown + (i+1));
					indices[indI++] = (short)(vPositionDown + i);
					
					//up indices
					indices[indI++] = (short)(vPositionUp + dotsPerCircleNum + 1 +  i);
					indices[indI++] = (short)(vPositionUp + i);
					indices[indI++] = (short)(vPositionUp + dotsPerCircleNum + 1 + (i+1));
					
					indices[indI++] = (short)(vPositionUp + i);					
					indices[indI++] = (short)(vPositionUp + (i+1));
					indices[indI++] = (short)(vPositionUp + dotsPerCircleNum + 1 + (i+1));							
				}
				angle += step;
			}
			addVertice(vertices, -currentR, y, 0, vPositionUp, dotsPerCircleNum, true);
			if (j > 0) {
				addVertice(vertices, -currentR, -y, 0, vPositionDown, dotsPerCircleNum, true);
			}
			
			vPositionUp -= dotsPerCircleNum + 1;
			vPositionDown += dotsPerCircleNum + 1;
			
			mainAngle += step;
        }

		mMesh = new Mesh(true, numVertices, numIndices, 
				new VertexAttribute(Usage.Position, 3, "aPosition"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "aTexCoords"));

		
		mMesh.setIndices(indices);
		mMesh.setVertices(vertices);
		return mMesh;
	}
}
