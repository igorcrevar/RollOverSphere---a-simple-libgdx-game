package com.igorcrevar.rolloverchuck.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CollisionHelper {
	private static Vector2 segmentV = new Vector2();
	private static Vector2 ptV = new Vector2();
	private static Vector2 tmpV2 = new Vector2();
	private static Vector2 closestPointOnSegment(Vector2 segA, Vector2 segB, Vector2 circleCenter) {
		segmentV.set(segB).sub(segA);
		ptV.set(circleCenter).sub(segA);
        float segmentVLen = segmentV.len();
        if (segmentVLen == 0f) {
        	throw new IllegalArgumentException("segA == segB for closestPointOnSegment");
        }
        
        Vector2 segVUnit = segmentV.scl(1 / segmentVLen);
        float proj = ptV.dot(segVUnit);
        if (proj <= 0f) {
        	return segA;
        }
        if (proj >= segmentVLen) {
        	return segB;
        }
        
        segVUnit.scl(proj).add(segA);
        return segVUnit;
	 }

	 public static boolean intersection(Vector2 segA, Vector2 segB, Vector3 circleCenter, float circleRadius) {
		tmpV2.set(circleCenter.x, circleCenter.z);
		return intersection(segA, segB, tmpV2, circleRadius);
	 }
	
	 public static boolean intersection(Vector2 segA, Vector2 segB, Vector2 circleCenter, float circleRadius) {
		Vector2 closest = closestPointOnSegment(segA, segB, circleCenter);
		tmpV2.set(circleCenter); // for god sake
		tmpV2.sub(closest);
		float len = tmpV2.len();
		if (len > circleRadius) {
			return false; // no collision
		}
		if (len == 0) {
			return false;
		}
		
		return true;
		 /*closest = closest_point_on_seg(seg_a, seg_b, circ_pos)
		dist_v = circ_pos - closest
		if dist_v.len() > circ_rad:
			return vec(0, 0)
		if dist_v.len() <= 0:
			raise ValueError, "Circle's center is exactly on segment"
		offset = dist_v / dist_v.len() * (circ_rad - dist_v.len())
		return offset*/
	 }
	
}
