package com.igorcrevar.rolloverchuck.utils;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/// Does not work with wrapped textures!
public class MyFontDrawer implements Disposable {
	private String value;
	private Mesh mesh;
	private float partWidth;
	private float partHeight;
	private float letterPadding;
	private float cellPadding;
	private int visibleRectCnt = -1;
	private Matrix4 viewModelMatrix = new Matrix4();
	private Matrix4 rotTmpMatrix = new Matrix4();
	
	private float xPosition;
	private float yPosition;
	private float width;
	private float height;
	private boolean isEnabled;
	private Vector2 uvMin = new Vector2(0f, 0f);
	private Vector2 uvMax = new Vector2(1f, 1f);
	private float uCellLen;
	private float vCellLen;
	
	public MyFontDrawer(String value, 
			float width, float height, float letterPadding, float cellPadding) {
		this(value, width, height, letterPadding, cellPadding, 1f, 1f);
	}
	
	public MyFontDrawer(String value, 
			float width, float height, float letterPadding, float cellPadding, float uCellLen, float vCellLen) {
		
		this.value = value;
		this.partWidth= width;
		this.partHeight = height;
		this.letterPadding = letterPadding;
		this.cellPadding = cellPadding;
		setUVCelLen(uCellLen, vCellLen);
		this.xPosition = 0f;
		this.yPosition = 0f;
		this.isEnabled = true;
	}
	
	public MyFontDrawer(String value, 
			float width, float height, float letterPadding, float cellPadding, TextureRegion tr, float xPosition, float yPosition, float uCellLen, float vCellLen) {
		this(value, width, height, letterPadding, cellPadding, uCellLen, vCellLen);
		setUVMinMax(tr);
		this.xPosition = xPosition;   
		this.yPosition = yPosition;
	}
	
	public MyFontDrawer init(IMyFontDrawerFont font) {
		if (Float.isNaN(uCellLen)) {
			uCellLen = 1.0f / font.getCharWidth();
		}
		
		if (Float.isNaN(vCellLen)) {
			vCellLen = 1.0f / font.getCharHeight();
		}
		
		float minX = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = Float.MIN_VALUE;
		
		this.calculateVisibleRectangles(font);
		mesh = new Mesh(true, getVertexCount(), getIndicesCount(),
                new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
		
		int vertexCount = getVertexCount();
		int indicesCount = getIndicesCount();
		
		short vIndex = 0;
		short iIndex = 0;
		float[] vertices = new float[vertexCount * 4];
		short[] indices = new short[indicesCount];
		
		byte numberOfSpaces = 0;
		float x = 0, y = 0;
		
		for (int i = 0; i < value.length(); ++i) {
			char c = value.charAt(i);
			if (c == ' ') {
				++numberOfSpaces;
				continue;
			}
			
			for (int row = 0; row < font.getCharHeight(); ++row) {
				for (int col = 0; col < font.getCharWidth(); ++col) {
					if (font.isSet(c, row, col)) {							
						x = xPosition;
						if (col > 0) {
							x += (cellPadding + partWidth) * col;
						}
						if (i > 0) {
							x += (letterPadding + (font.getCharWidth() - 1) * (cellPadding + partWidth) + partWidth)  * (i - numberOfSpaces);
							x += numberOfSpaces * partWidth * 2;
						}
						
						y = yPosition;
						if (row > 0) {
							y -= (cellPadding + partHeight) * row;
						}
						
						float uStart = col * uCellLen;
						float uEnd = uStart + uCellLen;
						if (uEnd > 1.0f) {
							uEnd = 1.0f;
							uStart = 1.0f - uCellLen;
						}
						
						float vStart = row * vCellLen;
						float vEnd = vStart + vCellLen;
						if (vEnd > 1.0f) {
							vEnd = 1.0f;
							vStart = 1.0f - vCellLen;
						}
						
						short vIndexReal = (short)(4 * vIndex);
						vertices[vIndexReal++] = x;   
						vertices[vIndexReal++] = y;
						vertices[vIndexReal++] = uStart;
						vertices[vIndexReal++] = vStart;														
						vertices[vIndexReal++] = x + partWidth;   
						vertices[vIndexReal++] = y;
						vertices[vIndexReal++] = uEnd;
						vertices[vIndexReal++] = vStart;
						vertices[vIndexReal++] = x + partWidth;
						vertices[vIndexReal++] = y - partHeight;
						vertices[vIndexReal++] = uEnd;
						vertices[vIndexReal++] = vEnd;							
						vertices[vIndexReal++] = x;   
						vertices[vIndexReal++] = y - partHeight;
						vertices[vIndexReal++] = uStart;
						vertices[vIndexReal++] = vEnd;					
						
						indices[iIndex] = vIndex;
						indices[iIndex + 1] = (short)(vIndex + 1);
						indices[iIndex + 2] = (short)(vIndex + 2);
						indices[iIndex + 3] = (short)(vIndex);
						indices[iIndex + 4] = (short)(vIndex + 2);
						indices[iIndex + 5] = (short)(vIndex + 3);
								
						vIndex += 4;
						iIndex += 6;
						
						minX = Math.min(x, minX);
						maxX = Math.max(x + partWidth, maxX);
						minY = Math.min(y - partHeight, minY);
						maxY = Math.max(y, maxY);
					}
				}
			}
		}
		
		if (minX == Float.MAX_VALUE) {
			width = height = 0.0f;
		}
		else {
			width = Math.abs(maxX - minX);
			height = Math.abs(maxY - minY);	
		}
		
		// set vertices and indexes
		mesh.setIndices(indices);
		mesh.setVertices(vertices);
		return this;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public String getValue() {
		return value;
	}
	
	public MyFontDrawer idt() {
		viewModelMatrix.idt();
		return this;
	}
	
	public MyFontDrawer translate(float x, float y, float z) {
		viewModelMatrix.translate(x, y, z);
		return this;
	}
	
	public MyFontDrawer scale(float x, float y, float z) {
		viewModelMatrix.scale(x, y, z);
		return this;
	}
	
	
	/**
	 * Rotate text around desired axis for desired angle in degrees
	 * @param x axis of rotation x coord
	 * @param y axis of rotation Y coord
	 * @param z axis of rotation Z coord
	 * @param degrees angle in degrees
	 * @return this object
	 */
	public MyFontDrawer rotateAround(float x, float y, float z, float degrees) {
		rotTmpMatrix.setToRotation(x, y, z, degrees);
		viewModelMatrix.mul(rotTmpMatrix);
		return this;
	}
	
	public void draw(ShaderProgram sp) {
		sp.setUniformf("u_uvMin", this.uvMin);
		sp.setUniformf("u_uvMax", this.uvMax);
		mesh.render(sp, GL20.GL_TRIANGLES);
	}

	private int getVertexCount() {
		return visibleRectCnt * 4;
	}
	
	private int getIndicesCount() {
		return visibleRectCnt * 6;
	}
	
	private void calculateVisibleRectangles(IMyFontDrawerFont font) {
		visibleRectCnt = 0;
		for (int i = 0; i < value.length(); ++i) {
			char c = value.charAt(i);
			for (int j = 0; j < font.getCharHeight(); ++j) {
				for (int k = 0; k < font.getCharWidth(); ++k) {
					if (font.isSet(c, j, k)) {
						++visibleRectCnt;
					}
				}
			}
		}
	}
	
	public void dispose() {
		 mesh.dispose();
	}

	public Matrix4 getViewModelMatrix() {
		return viewModelMatrix;
	}
	
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public boolean getIsEnabled() {
		return this.isEnabled;
	}
	
	public MyFontDrawer setUVMinMax(float u1, float v1, float u2, float v2) {
		uvMin.set(u1, v1);
		uvMax.set(u2, v2);
		return this;
	}
	
	public MyFontDrawer setUVMinMax(TextureRegion tr) {
		uvMin.set(tr.getU(), tr.getV());
		uvMax.set(tr.getU2(), tr.getV2());
		return this;
	}
	
	public MyFontDrawer setUVCelLen(float uCellLen, float vCellLen) {
		this.uCellLen = uCellLen;
		this.vCellLen = vCellLen;
		if (uCellLen < 0.01f || uCellLen > 1.0f || vCellLen < 0.01f || vCellLen > 1.0f) {
			throw new IllegalArgumentException("MyFontDrawer");
		}
		return this;
	}

	public void setUVMinMaxScrollU(TextureRegion tr, float uPos, float uScroll) {
		setUVMinMax(tr);
		float dist = uvMax.x - uvMin.x; 
		float newMin = uPos * dist + uvMin.x;
		float newMax = uScroll * dist + newMin;
		uvMin.x = newMin;
		uvMax.x = newMax;
	}
	
	public void setUVMinMaxScrollV(TextureRegion tr, float vPos, float vScroll) {
		setUVMinMax(tr);
		float dist = uvMax.y - uvMin.y; 
		float newMin = vPos * dist + uvMin.y;
		float newMax = vScroll * dist + newMin;
		uvMin.y = newMin;
		uvMax.y = newMax;
	}
}
