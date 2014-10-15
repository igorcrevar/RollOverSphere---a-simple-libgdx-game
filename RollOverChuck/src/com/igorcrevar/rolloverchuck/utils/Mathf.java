package com.igorcrevar.rolloverchuck.utils;

public class Mathf {
	public static float lerpBI(float a, float b, float time){
		if (time < 0.5f) {
			return lerp(a, b, time * 2.0f);
		}
		return lerp(b, a, (time - 0.5f) * 2.0f);
	}
	
	public static float lerp(float a, float b, float time){
		return lerp(a, b, time, false);
	}
	
	public static float lerp(float a, float b, float time, boolean inverse) {
		if (time < 0.0f) time = 0.0f;
		if (time > 1.0f) time = 1.0f;
		return !inverse ? a + (b - a) * time : b + (a - b) * time;
	}
}
