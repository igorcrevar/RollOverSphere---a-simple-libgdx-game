package com.igorcrevar.rolloverchuck.utils;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;

public class MyFontDrawerBatch implements Disposable {
	private IMyFontDrawerFont myFont;
	private ArrayList<MyFontDrawer> fonts = new ArrayList<MyFontDrawer>(8);
	private Matrix4 projectionMatrix = new Matrix4();
	private float width;
	private float height;
	
	public MyFontDrawerBatch(IMyFontDrawerFont myFont, float width, float height) {
		this.myFont = myFont;
		
		this.width = width;
		this.height = height;
	}
	
	public MyFontDrawer addNew(MyFontDrawer drawer) {
		fonts.add(drawer);
		drawer.init(myFont);
		return drawer;
	}
	
	public void draw(ShaderProgram sp, Texture txt) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		txt.bind();
		sp.begin();
		sp.setUniformi("u_texture", 0);		
		for (MyFontDrawer fd : fonts) {
			if (fd.getIsEnabled()) {
				projectionMatrix.setToOrtho2D(0, 0, width, height);
				sp.setUniformMatrix("u_projTrans", projectionMatrix.mul(fd.getViewModelMatrix()));
				fd.draw(sp);
			}
		}
		sp.end();
	}

	@Override
	public void dispose() {
		for (MyFontDrawer fd : fonts) {
			fd.dispose();
		}
	}
}
