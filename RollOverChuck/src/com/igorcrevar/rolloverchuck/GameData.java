package com.igorcrevar.rolloverchuck;

import com.badlogic.gdx.math.Vector3;

public class GameData {
	public static Vector3 ObjectZero = new Vector3(0.0f, -1.5f, -13);
	public float FieldSize = 40.0f;	
	public float CubeScale = 2.0f; 
	public float CubeRegionSize = 4.0f;
	
	public float Chuck_r = 2.5f;
	public float Chuck_rBig = 4.0f;
	public float Chuck_rSmall = 1.5f;
	public float ChuckRadiusChangeTime = 10f;

	public float BoxDistanceSquared = 18.0f * 18.0f;
	public int PointMaxMultiplicator = 1024;
	
	public Vector3 ChuckBoundaries = new Vector3(FieldSize / 2.0f, 0f, FieldSize / 2.0f);
	public Vector3 ChuckBouncingFactor = new Vector3(0.7f, 0f, 0.7f);
	
	public long AdShowOn = 4;
	public float MinTimeCountAsOnePlay = 10f;
	
	// settings for game types
	public int ArcadeGameTimer = 64;	
	
	// settings regarding level
	public Vector3 ChuckMaxVelocity = new Vector3();
	public Vector3 ChuckFriction = new Vector3();
	public float ChuckAcc = 0.0f;
	public float BoxGenerationTime = 0.0f;
	public float BoxTimeToLive = 3.0f;
	public float BoxBonusBoxGenerationTime = 0;
	public float BoxNegativeBoxGenerationTime = 0;
	public float PointMultiplicatorTime = 0.2f;
	
	// settings for easy level
	public Vector3 ChuckMaxVelocityEasy = new Vector3(200.0f, 0.0f, 200.0f);
	public Vector3 ChuckFrictionEasy = new Vector3(20.0f, 0.0f, 20.0f);
	public float ChuckAccEasy = 120.0f;
	public float BoxGenerationTimeEasy = 1.2f;
	public float BoxTimeToLiveEasy = 2.5f;
	public float BoxBonusBoxGenerationTimeEasy = 10.0f;
	public float BoxNegativeBoxGenerationTimeEasy = 1000000.0f;
	public float PointMultiplicatorTimeEasy = 0.2f;

	// settings for medium level
	public Vector3 ChuckMaxVelocityMedium = new Vector3(160f, 0f, 160f);
	public Vector3 ChuckFrictionMedium = new Vector3(25.0f, 0.0f, 25.0f);
	public float ChuckAccMedium = 100.0f;
	public float BoxGenerationTimeMedium = 1.4f;
	public float BoxTimeToLiveMedium = 2.3f;
	public float BoxBonusBoxGenerationTimeMedium = 15.0f;
	public float BoxNegativeBoxGenerationTimeMedium = 1000000.0f;
	public float PointMultiplicatorTimeMedium = 0.2f;
	
	// settings for medium level
	public Vector3 ChuckMaxVelocityHard = new Vector3(160.0f, 0.0f, 160.0f);
	public Vector3 ChuckFrictionHard = new Vector3(30.0f, 0.0f, 30.0f);
	public float ChuckAccHard = 100.0f;
	public float BoxGenerationTimeHard = 1.4f;
	public float BoxTimeToLiveHard = 2.1f;
	public float BoxBonusBoxGenerationTimeHard = 20.0f;
	public float BoxNegativeBoxGenerationTimeHard = 2.0f;
	public float PointMultiplicatorTimeHard = 0.4f;
	
	public GameData() {
		//GameHelper.load("chuck.txt", this);
	}
	
	public void initForDifficulty(GameType gameType) {
		switch (gameType.getDifficulty()) {
		case Easy:
			ChuckMaxVelocity = ChuckMaxVelocityEasy;
			ChuckFriction = ChuckFrictionEasy;
			ChuckAcc = ChuckAccEasy;
			BoxGenerationTime = BoxGenerationTimeEasy;
			BoxBonusBoxGenerationTime = BoxBonusBoxGenerationTimeEasy;
			BoxNegativeBoxGenerationTime = BoxNegativeBoxGenerationTimeEasy;
			PointMultiplicatorTime = PointMultiplicatorTimeEasy;
			BoxTimeToLive = BoxTimeToLiveEasy;
			break;
		case Medium:
			ChuckMaxVelocity = ChuckMaxVelocityMedium;
			ChuckFriction = ChuckFrictionMedium;
			ChuckAcc = ChuckAccMedium;
			BoxGenerationTime = BoxGenerationTimeMedium;
			BoxBonusBoxGenerationTime = BoxBonusBoxGenerationTimeMedium;
			BoxNegativeBoxGenerationTime = BoxNegativeBoxGenerationTimeMedium;
			PointMultiplicatorTime = PointMultiplicatorTimeMedium;
			BoxTimeToLive = BoxTimeToLiveMedium;
			break;
		case Hard:
			ChuckMaxVelocity = ChuckMaxVelocityHard;
			ChuckFriction = ChuckFrictionHard;
			ChuckAcc = ChuckAccHard;
			BoxGenerationTime = BoxGenerationTimeHard;
			BoxBonusBoxGenerationTime = BoxBonusBoxGenerationTimeHard;
			BoxNegativeBoxGenerationTime = BoxNegativeBoxGenerationTimeHard;
			PointMultiplicatorTime = PointMultiplicatorTimeHard;
			BoxTimeToLive = BoxTimeToLiveHard;
			break;
		}
	}

	public int getRegionsSize() {
		return (int)(FieldSize / CubeRegionSize);
	}
}
