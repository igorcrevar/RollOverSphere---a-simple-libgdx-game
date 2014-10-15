package com.igorcrevar.rolloverchuck.utils;

import java.lang.reflect.Field;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class GameHelper {
	public static float screenX2OtherX(float x, float worldX) {
		return worldX * x / Gdx.graphics.getWidth();
	}
	
	public static float screenY2OtherY(float y, float worldY) {
		return worldY * (Gdx.graphics.getHeight() - y) / Gdx.graphics.getHeight();
	}
	
	public static void clearScreen() {
		Gdx.graphics.getGL20().glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.graphics.getGL20().glClearDepthf(1.0f);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	public static boolean tapPointInsideRectangle(float x, float y, float rectX, float rectY, float rectWidth, float rectHeight) {
		return x >= rectX && x <= rectX + rectWidth && y >= rectY - rectHeight && y <= rectY;
	}
	
	public static float getAlignedPosX(BitmapFont font, String txt, float sizeX) {
		return (sizeX - font.getBounds(txt).width) / 2.0f;
	}
	
	public static float getAlignedPosY(BitmapFont font, String txt, float sizeY) {
		return (sizeY - font.getBounds(txt).height) / 2.0f;
	}
	
	public static void setProjectionFor2D(SpriteBatch spriteBatch, float width, float height) {
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
	
	private static Vector3 tmp1 = new Vector3();
	private static Vector3 tmp2 = new Vector3();
	private static Vector3 tmp3 = new Vector3();
	/**
	 * populate vertex buffer with normals
	 * @param vert vertex buffer
	 * @param ind index buffer
	 * @param vertexSize size of one vertex
	 * @param posOffset offset of position data inside vertex
	 * @param normalOffset offset of normal data inside vertex
	 */
	public static void populateNormals(float[] vert, short[] ind, int vertexSize, int posOffset, int normalOffset) {
		for (int i = 0; i < ind.length; i += 3) {
			int off1 = vertexSize * ind[i];
			int off2 = vertexSize * ind[i + 1];
			int off3 = vertexSize * ind[i + 2];
			// set vertex positions
			tmp1.set(vert[off1 + posOffset], vert[off1 + posOffset + 1], vert[off1 + posOffset + 2]);
			tmp2.set(vert[off2 + posOffset], vert[off2 + posOffset + 1], vert[off2 + posOffset + 2]);
			tmp3.set(vert[off3 + posOffset], vert[off3 + posOffset + 1], vert[off3 + posOffset + 2]);
			// subs
			tmp2.sub(tmp1);
			tmp3.sub(tmp1);
			// cross
			tmp2.crs(tmp3).nor();
			// save normals
			vert[off1 + normalOffset] = tmp2.x;
			vert[off1 + normalOffset + 1] = tmp2.y;
			vert[off1 + normalOffset + 2] = tmp2.z;
		}
	}
	
	private static String lastStrValue = null;
	private static long lastPointsValue = Long.MIN_VALUE;
	public static String getStringPoints(long pointsScore) {
		if (pointsScore == lastPointsValue) {
			return lastStrValue;
		}
		else if (pointsScore <= 99999999l) {
			lastStrValue = String.valueOf(pointsScore);
		}
		else if (pointsScore <= 99999999999l) {
			lastStrValue = String.valueOf(pointsScore / 1000l) + "K";
		}
		else if (pointsScore <= 99999999999999l) {
			lastStrValue = String.valueOf(pointsScore / 1000000l) + "M";
		}
		else {
			lastStrValue = String.valueOf(pointsScore / 1000000000l) + "G";
		}
		
		lastPointsValue = pointsScore;
		return lastStrValue;
	}
	
	public static void load(String fileName, Object object) {
		try {
			FileHandle fh = Gdx.files.external(fileName);
			Field[] fields = object.getClass().getFields();			
			if (fh.exists()) {
				String content = fh.readString();
				String[] rows = content.split("\n");
				for (String r : rows) {
					String[] data = r.split("=");
					if (data.length != 2) continue; 
					for (Field f : fields) {
						Class<?> type = f.getType();						
						if (f.getName().equals(data[0])) {
							if (type.isPrimitive()) {
								if (type.equals(boolean.class)) {
									f.setBoolean(object, Boolean.parseBoolean(data[1]));
								}
								else if (type.equals(float.class)) {
									f.setFloat(object, Float.parseFloat(data[1]));
								}
								else if (type.equals(int.class)) {
									f.setInt(object, Integer.parseInt(data[1]));
								}
								else if (type.equals(long.class)) {
									f.setLong(object, Long.parseLong(data[1]));
								}
							}
							else if (type.getName().equals("com.badlogic.gdx.math.Vector3")) {
								String[] tmp = data[1].split(",");
								f.set(object, new Vector3(Float.parseFloat(tmp[0]), Float.parseFloat(tmp[1]), Float.parseFloat(tmp[2])));
							}
							break;
						}
					}
				}
			}
			else {
				StringBuilder content = new StringBuilder();
				for (Field f : fields) {
					content.append(f.getName()).append('=');
					Class<?> type = f.getType();
					String tmp = "";
					if (type.equals(boolean.class)) {
						tmp = Boolean.toString(f.getBoolean(object));
					}
					else if (type.equals(float.class)) {
						tmp = Float.toString(f.getFloat(object));
					}
					else if (type.equals(int.class)) {
						tmp = Integer.toString(f.getInt(object));
					}
					else if (type.equals(long.class)) {
						tmp = Long.toString(f.getLong(object));
					}
					else if (type.getName().equals("com.badlogic.gdx.math.Vector3")) {
						Vector3 v = (Vector3)f.get(object);
						tmp = v.x + "," + v.y + "," + v.z;						
					}
					
					content.append(tmp).append("\n");
				}

				fh.writeString(content.toString(), false);
			}
		}
		catch (Exception ex) {			
		}		
	}
}
