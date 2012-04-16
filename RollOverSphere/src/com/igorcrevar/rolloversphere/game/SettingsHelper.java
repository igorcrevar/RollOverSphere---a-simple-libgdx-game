package com.igorcrevar.rolloversphere.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class SettingsHelper {
	private final static String mFile = ".punchthecircle";
	public static boolean soundEnabled = true; //doest matter right now
	public static boolean isAcceleatorEnabled = true;
	public static float arcadeGameTimeout = 31.0f; //in seconds
	public static int[] highscores = new int[] { 100, 80, 50 }; //arcade, chalenge, freeplay

	public static void load () {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(Gdx.files.external(mFile).read()));
			soundEnabled = Boolean.parseBoolean(in.readLine());
			isAcceleatorEnabled = Boolean.parseBoolean(in.readLine());
			for (int i = 0; i < highscores.length; i++) {
				highscores[i] = Integer.parseInt(in.readLine());
			}		
		} catch (Throwable e) {
		}
		finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
			}
		}
	}

	public static void save () {
		BufferedWriter out = null;
		try {
			char lineEnd = '\n';
			FileHandle fh = Gdx.files.external(mFile);
			out = new BufferedWriter(new OutputStreamWriter(fh.write(false)));
			out.write(Boolean.toString(soundEnabled));
			out.write(lineEnd);
			out.write(Boolean.toString(isAcceleatorEnabled));
			out.write(lineEnd);
			for (int i = 0; i < highscores.length; i++) {
				out.write(Integer.toString(highscores[i]));
				out.write(lineEnd);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {
			}
		}
	}
	
	public static void addScore (int score, GameTypes gameType) {
		if (score > highscores[gameType.ordinal()]){
			highscores[gameType.ordinal()] = score;
		}
	}
	
	public static int getScore(GameTypes gameType){
		return highscores[gameType.ordinal()];
	}
}
