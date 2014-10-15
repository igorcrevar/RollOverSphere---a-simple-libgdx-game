package com.igorcrevar.rolloverchuck.objects.boxes;

import com.badlogic.gdx.graphics.Color;
import com.igorcrevar.rolloverchuck.objects.ChuckObject;

public interface IBoxType {
	public Color getColor();
	public float getScale();
	public int getPoint();
	public void applyUpgrade(ChuckObject chuck);
	public float getRotationSpeed();
}
