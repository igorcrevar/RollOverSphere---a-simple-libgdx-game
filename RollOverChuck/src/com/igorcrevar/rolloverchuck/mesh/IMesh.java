package com.igorcrevar.rolloverchuck.mesh;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public interface IMesh {
	void draw(ShaderProgram shaderProgram);
	void dispose();
}
