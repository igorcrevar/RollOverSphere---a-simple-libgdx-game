package com.igorcrevar.rolloverchuck;

public class GameType
{
	public static enum BaseGameType { Arcade, StressFree };
	public static enum Difficulty { Easy, Medium, Hard }

	private BaseGameType baseType;
	private Difficulty difficulty;;
	
	public GameType() {
	}
	
	public GameType set(BaseGameType baseType, Difficulty diff) {
		this.baseType = baseType;
		this.difficulty = diff;
		return this;
	}
	
	public BaseGameType getBaseType() {
		return baseType;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	@Override
	public String toString() {
		return String.format("%d-%d", baseType.ordinal(), difficulty.ordinal());
	}
	
	public static String toString(BaseGameType bt, Difficulty diff) {
		return String.format("%d-%d", bt.ordinal(), diff.ordinal());
	}
}
 
