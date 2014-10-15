package com.igorcrevar.rolloverchuck.utils;

public interface IMyFontDrawerFont {
	byte getCharWidth();
	byte getCharHeight();
	char getInitialChar();
	byte[][] getCharset();
	boolean isSet(char character, int row, int col);
}
