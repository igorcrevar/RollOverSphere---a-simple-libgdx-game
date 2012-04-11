package com.igorcrevar.rolloversphere.collsion;

import com.badlogic.gdx.math.Rectangle;

public class OverlapTester {
	/**
	 * Check if Point with coords x and y is inside Rectangle r
	 * @param r
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean pointInRectangle (Rectangle r, float x, float y) {
		return r.x <= x && r.x + r.width >= x && r.y <= y && r.y + r.height >= y;
	}
}
