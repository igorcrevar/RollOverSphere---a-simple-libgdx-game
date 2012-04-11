package com.igorcrevar.rolloversphere;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.backends.jogl.JoglApplicationConfiguration;
import com.igorcrevar.rolloversphere.game.RollOverSphere;

public class RollOverSphereDesktop {
	private static DisplayMode getBestMode(int width, int height){
		DisplayMode[] modes = JoglApplicationConfiguration.getDisplayModes();
		DisplayMode mode = null;
		float bestdiff = 10000000;
		for (DisplayMode m: modes){
			float diff = (float)Math.sqrt(Math.pow(width - m.width, 2) + Math.pow(height - m.height, 2));
			if (diff < bestdiff){
				bestdiff = diff;
				mode = m;
			}
		}		
		return mode;
	}
	
	public static void main(String[] argv) {
		int w, h;
		w = 320; h = 240;
		//w = 480; h = 320;
		w = 800; h = 480;
		JoglApplicationConfiguration config = new JoglApplicationConfiguration();
		config.title ="Punch the circle!";
	    config.setFromDisplayMode(JoglApplicationConfiguration.getDesktopDisplayMode());
		config.width = w;
		config.height = h;
		config.fullscreen = false;
		config.useGL20 = false;
		
		new JoglApplication(new RollOverSphere(), config);
	}
}
