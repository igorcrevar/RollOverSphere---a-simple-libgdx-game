package com.igorcrevar.rolloverchuck;

public interface IScene {
	void init(ISceneManager sceneManager);
	void update(ISceneManager sceneManager, float deltaTime);
	void dispose(ISceneManager sceneManager);
	void leave(ISceneManager sceneManager);	
}
