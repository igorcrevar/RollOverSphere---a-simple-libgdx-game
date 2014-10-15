package com.igorcrevar.rolloverchuck.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.igorcrevar.rolloverchuck.GameData;
import com.igorcrevar.rolloverchuck.GameManager;

public class TrailObject {
	private static final int MAX_QUADS = 80;
	private static final int QUAD_SIZE = 6 * 3;
	private static final int FRAME_SKIP = 4;
	
	private Mesh mesh = new Mesh(false, MAX_QUADS * 6, 0, VertexAttribute.Position()); 
	private ShaderProgram shader;
	private int currentQuads;
	private float[] vertices = new float[MAX_QUADS * QUAD_SIZE];
	private Color colorFactor = new Color(1.0f, 1.0f, 1.0f, 0.5f);
	private Vector3 tmp = new Vector3();
	private Vector3 tmp2 = new Vector3();
	private int frameSkipper = 0;
	
	public TrailObject(GameManager gameManager) {
		shader = gameManager.getShader("simple");	
	}
	
	public void init() {
		currentQuads = 0;
		frameSkipper = 0;
	}
	
	public void update(ChuckObject co, float deltaTime) {
		/*++frameSkipper;
		if (frameSkipper < FRAME_SKIP) {
			continue;
		}*/
		
		frameSkipper = 0;
		Vector3 currPos = co.getCurrentPosition();
		Vector3 prevPos = co.getPrevFramePosition();
		if (!currPos.idt(prevPos)) {
			float radius = co.getCurrentRadius();
			// tmp will have direction of moving 
			tmp.set(prevPos).sub(currPos).nor();
			tmp.scl(radius * 0.8f);			
			// vector tmp and tmp2 must be orthogonal (perpendicular)
			// so their dot product equals to zero
			// y coordinate doesn't matter set it to 0
			// we have one "jednacina" with two variables
			// but what is meter that tmp2 must be direction vector (normalized)
			// so we guess some value for X and calculate Z component according to chosen X
			// calculate "Left" orthogonal ("right" is -"left")
			if (tmp.z != 0) {
				tmp2.set(-1f, 0f, 0f);
				tmp2.z = (-tmp2.x * tmp.x) / tmp.z;
			}
			else {
				tmp2.set(0f, 0f, 1f);
			}
				
			// don't want trail to be equal width of chuck
			tmp2.nor().scl(radius * 0.75f);
			
			// move current vertices if needed;
			if (currentQuads > 0) {
				int len = Math.min(MAX_QUADS - 1, currentQuads);
				System.arraycopy(vertices, 0, vertices, QUAD_SIZE, len * QUAD_SIZE);
			}
			
			Vector3 zeroPos = GameData.ObjectZero;
			float defY = radius * 1.5f + zeroPos.y;
			vertices[0] = currPos.x + zeroPos.x + tmp.x + tmp2.x; 
			vertices[1] = defY;
			vertices[2] = currPos.z + zeroPos.z + tmp.z + tmp2.z;
			vertices[3] = vertices[9] = currPos.x + zeroPos.x + tmp.x - tmp2.x; 
			vertices[4] = vertices[10] = defY;
			vertices[5] = vertices[11] = currPos.z + zeroPos.z + tmp.z - tmp2.z;
			vertices[6] = vertices[15] = vertices[QUAD_SIZE];
			vertices[7] = vertices[16] = vertices[QUAD_SIZE + 1];
			vertices[8] = vertices[17] = vertices[QUAD_SIZE + 2];
			vertices[12] = vertices[QUAD_SIZE + 3];
			vertices[13] = vertices[QUAD_SIZE + 4];
			vertices[14] = vertices[QUAD_SIZE + 5];
//			else {
//				vertices[6] = vertices[15] = prevPos.x + zeroPos.x + tmp2.x; 
//				vertices[7] = vertices[16] = defY;
//				vertices[8] = vertices[17] = prevPos.z + zeroPos.z + tmp2.z;
//				vertices[12] = prevPos.x + zeroPos.x - tmp2.x; 
//				vertices[13] = defY;
//				vertices[14] = prevPos.z + zeroPos.z - tmp2.z;
//			}
			
			// increment number of quads			
			currentQuads = Math.min(currentQuads + 1, MAX_QUADS);
			// UPDATE MESH
			mesh.setVertices(vertices);
			// mesh.updateVertices(0, vertices);//
		}		
		else {
			currentQuads = Math.max(currentQuads - 1, 0);
		}
	}
	
	public void draw(Matrix4 projViewMatrix) {
		if (currentQuads > 1) {
			shader.begin();
			shader.setUniformMatrix("u_projectionViewMatrix",  projViewMatrix);
			shader.setUniformf("u_color_factor", colorFactor);
			mesh.render(shader, GL20.GL_TRIANGLES, 0, (currentQuads - 1) * QUAD_SIZE);
			shader.end();
		}		
	}
	
	public void dispose() {
		mesh.dispose();
	}
	
	// tmp2 will have 0-th dot of quad
	//float angle = (float) Math.atan2(currPos.z, currPos.x);
	//tmp2.set(0.0f, 0.0f, -1.0f);
	//tmp2.x = (float)(tmp2.x * Math.cos(angle) - tmp2.z * Math.sin(angle));
	//tmp2.z = (float)(tmp2.z * Math.cos(angle) + tmp2.x * Math.sin(angle));
	//tmp2.scl(radius);
	
}
