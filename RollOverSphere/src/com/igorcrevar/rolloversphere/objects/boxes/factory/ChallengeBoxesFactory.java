package com.igorcrevar.rolloversphere.objects.boxes.factory;

import com.igorcrevar.rolloversphere.objects.boxes.Box;
import com.igorcrevar.rolloversphere.objects.boxes.UpgradeDirection;
import com.igorcrevar.rolloversphere.objects.boxes.UpgradeSpeed;

public class ChallengeBoxesFactory implements IBoxesFactory{

	@Override
	public Box get() {
		Box pb;
		int no = (int)(Math.random() * 36);
		if (no < 7){
			pb = new Box(new float[] { 0.3f, 1.0f, 0.5f, 1.0f }, 30, 0.5f, 100.0f);
		}
		else if (no < 18){
			pb = new Box(new float[] { 0.3f, 1.0f, 0.7f, 1.0f }, 25, 0.4f, 80.0f);
		}
		else if (no < 26){
			pb = new Box(new float[] { 0.0f, 1.0f, 0.8f, 1.0f }, 15, 0.2f, 60.0f);
		}
		else if (no < 35){
			pb = new UpgradeDirection();			
		}
		else {
			pb = new UpgradeSpeed();	
		}
		
		return pb;
	}

}
