package com.igorcrevar.rolloversphere.objects.boxes.factory;

import com.igorcrevar.rolloversphere.objects.boxes.Box;
import com.igorcrevar.rolloversphere.objects.boxes.UpgradeDirection;
import com.igorcrevar.rolloversphere.objects.boxes.UpgradeSpeed;

public class FreeplayBoxesFactory implements IBoxesFactory {

	@Override
	public Box get() {
		Box pb;
		int no = (int)(Math.random() * 20);
		if (no < 7){
			pb = new Box(new float[] { 0.9f, 0.90f, 0.3f, 1.0f }, 5, 0.0f, 40.0f);
		}
		else if (no < 18){
			pb = new Box(new float[] { 0.3f, 1.0f, 0.5f, 1.0f }, 15, 0.4f, 60.0f);
		}
		else if (no < 19){
			pb = new UpgradeDirection();			
		}
		else {
			pb = new UpgradeSpeed();	
		}
		
		return pb;
	}
}
