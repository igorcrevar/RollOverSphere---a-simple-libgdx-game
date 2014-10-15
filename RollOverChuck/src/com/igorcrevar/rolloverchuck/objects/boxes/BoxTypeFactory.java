package com.igorcrevar.rolloverchuck.objects.boxes;

import com.badlogic.gdx.graphics.Color;
import com.igorcrevar.rolloverchuck.objects.ChuckObject;

public class BoxTypeFactory {
	private static class StandardBox implements IBoxType {
		@Override
		public float getScale() {
			return 1.0f;
		}

		@Override
		public int getPoint() {			
			return 1;
		}

		@Override
		public void applyUpgrade(ChuckObject chuck) {			
		}

		@Override
		public Color getColor() {
			return new Color(0.0f, 0.2f, 0.7f, 1.0f);
		}

		@Override
		public float getRotationSpeed() {
			return 50.0f;
		}		
	}
	
	private static class GrowUpBox implements IBoxType {
		@Override
		public float getScale() {
			return 0.6f;
		}

		@Override
		public int getPoint() {			
			return 0;
		}

		@Override
		public void applyUpgrade(ChuckObject chuck) {
			chuck.makeChuckBig();
		}

		@Override
		public Color getColor() {
			return new Color(0f, 1.0f, 0.7f, 1.0f);
		}

		@Override
		public float getRotationSpeed() {
			return 100.0f;
		}		
	}
	
	private static class ShrinkBox implements IBoxType {
		@Override
		public Color getColor() {
			return new Color(1.0f, 0.9f, 0.0f, 1.0f);
		}		
		
		@Override
		public float getScale() {
			return 0.6f;
		}

		@Override
		public int getPoint() {			
			return 0;
		}

		@Override
		public void applyUpgrade(ChuckObject chuck) {
			chuck.makeChuckSmall();
		}
		
		@Override
		public float getRotationSpeed() {
			return 15.0f;
		}		
	}
	
	private static class BonusBox implements IBoxType {
		@Override
		public Color getColor() {
			return new Color(0.8f, 0.8f, 0.8f, 1.0f);
		}		
		
		@Override
		public float getScale() {
			return 0.6f;
		}

		@Override
		public int getPoint() {			
			return 24;
		}

		@Override
		public void applyUpgrade(ChuckObject chuck) {
		}
		
		@Override
		public float getRotationSpeed() {
			return 80.0f;
		}	
	}
	
	private static class NegativeBox implements IBoxType {
		@Override
		public Color getColor() {
			return new Color(1.0f, 0.0f, 0.f, 1.0f);
		}		
		
		@Override
		public float getScale() {
			return 1f;
		}

		@Override
		public int getPoint() {			
			return -4;
		}

		@Override
		public void applyUpgrade(ChuckObject chuck) {
		}
		
		@Override
		public float getRotationSpeed() {
			return 5.0f;
		}	
	}
	
	private static IBoxType[] types = new IBoxType[] { new StandardBox(), new GrowUpBox(), new ShrinkBox(), new BonusBox(), new NegativeBox() };
	
	public static IBoxType create(int type) {
		if (type < 0 || type >= types.length) {
			throw new IllegalArgumentException("Unknown type");			
		}
		return types[type];
	}
}
