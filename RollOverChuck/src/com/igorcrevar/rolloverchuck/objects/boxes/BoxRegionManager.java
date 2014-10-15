package com.igorcrevar.rolloverchuck.objects.boxes;

import java.util.Random;

import com.badlogic.gdx.math.Vector3;
import com.igorcrevar.rolloverchuck.GameData;

public class BoxRegionManager {
	private IBoxRegion[] regions;
	private IBoxRegion[] tmpRegions;
	private Vector3 tmpVector = new Vector3();
	private Random rnd = new Random();
	
	public BoxRegionManager(GameData gameData) {
		if ((int)gameData.FieldSize % (int)gameData.CubeRegionSize != 0) {
			throw new IllegalArgumentException();
		}
		
		int size = gameData.getRegionsSize();
		regions = new IBoxRegion[size * size];
		tmpRegions = new IBoxRegion[size * size];
		
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				float posX = -gameData.FieldSize / 2.0f + j * gameData.CubeRegionSize;
				float posY = -gameData.FieldSize / 2.0f + i * gameData.CubeRegionSize;
				regions[i * size + j] = new BoxRegion(posX, posY, gameData.CubeRegionSize * 0.5f);
			}
		}
	}
	
	public void init() {
		for (IBoxRegion br : regions) {
			br.free();
		}
	}
	
	public void markRegionAsNotFree(GameData gameData, int x, int y, int width, int height) {
		for (int i = y; i < y + height; ++i) {
			for (int j = x; j < x + width; ++j) {
				int offset = gameData.getRegionsSize() * i + j;
				if (offset < gameData.getRegionsSize()) {
					regions[offset].take();
				}
			}
		}		
	}
	
	public IBoxRegion getOne(Vector3 basePosition, float minDistanceSquared) {
		int numberAvailable = 0;
		for (IBoxRegion br : regions) {
			if (basePosition == null || br.isFreeAndNotNear(basePosition, tmpVector, minDistanceSquared)) {
				tmpRegions[numberAvailable] = br;
				++numberAvailable;
			}				
		}
		
		if (numberAvailable > 0) {
			int chs = rnd.nextInt(numberAvailable);
			return tmpRegions[chs];
		}
		
		return null;
	}
}
