package com.igorcrevar.rolloversphere.input;

public interface IMyInputAdapter {
	void onMove(int dx, int dy, int x, int y);
	void onClick(int x, int y);
	void onDoubleClick(int x, int y);
	void onBack();
}
