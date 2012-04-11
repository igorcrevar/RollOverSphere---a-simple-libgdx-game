package com.igorcrevar.rolloversphere.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.igorcrevar.rolloversphere.mesh_gl10.AbstractMesh;

public abstract class GameObject {
	/**
	 * one of: GL_POINTS, GL_LINE_STRIP, GL_LINE_LOOP, GL_LINES, GL_TRIANGLE_STRIP, GL_TRIANGLE_FAN, GL_TRIANGLES, GL_QUAD_STRIP 
	 */
	protected int mDrawArrayMode;
	protected AbstractMesh mMesh;
	protected Texture mTexture;
	protected float[] mColor;
	
	public float boundingSphereR;
	public Vector3 position = new Vector3(0.0f, 0.0f, 0.0f); 
	public Vector3 rotation = new Vector3(0.0f, 0.0f, 0.0f);
	public Vector3 scale = new Vector3(1.0f, 1.0f, 1.0f);
		
	public GameObject(){
	}
	
	public void init(){
		mMesh = getMesh();
		mDrawArrayMode = mMesh.getRenderTechnique();
		mTexture = getTexture();
	}

	public AbstractMesh getObjectMesh()
	{
		return mMesh;
	}
	
	protected abstract AbstractMesh getMesh();
	protected abstract Texture getTexture();
	public abstract void update(float timeDiff);
	
	public void render(Camera camera){
		preRender(camera);
		GL10 gl = Gdx.gl10;		
		if (mTexture != null){
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			mTexture.bind();
		}
		else{
			gl.glDisable(GL10.GL_TEXTURE_2D);
			gl.glColor4f(mColor[0], mColor[1], mColor[2], mColor[3]);
		}
		
	/*	gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadMatrixf(camera.projection.val, 0);
		gl.glMatrixMode(GL10.GL_MODELVIEW);		
		
		Matrix4 matrix = new Matrix4(camera.view.val);
		matrix.translate(position.x, position.y, position.z);
		
		Quaternion quat = Quaternion.idt();
		quat.setEulerAngles(rotation.y, rotation.x, rotation.z);
		Matrix4 matrixRot = new Matrix4(quat);
		
		gl.glLoadMatrixf(matrix.mul(matrixRot).val, 0);*/
		
		camera.apply(Gdx.gl10); //apply camera
	    gl.glTranslatef(position.x, position.y, position.z); //translate
		gl.glRotatef(rotation.x, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotation.y, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(rotation.z, 0.0f, 0.0f, 1.0f);
		gl.glScalef(scale.x, scale.y, scale.z);
        mMesh.getMesh().render(mDrawArrayMode);
        postRender();
	}
	
	protected void postRender() {
	}

	protected void preRender(Camera camera) {
		
	}
	
	public final void setScaleAndBoundingSphereR(float s, float baseSphereR){
		scale.set(s, s, s);
		boundingSphereR = baseSphereR * s;
	}
}
