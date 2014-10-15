#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP
#endif

attribute vec4 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_projectionViewMatrix;
uniform vec2 u_repeat;
uniform vec2 u_uvMin;
uniform vec2 u_uvMax;

varying vec2 v_texCoords;
varying vec2 v_repeat;
varying vec2 v_uvMin;
varying vec2 v_uvMax;


void main()
{
  	v_texCoords = a_texCoord0;
  	v_uvMin = u_uvMin;
  	v_uvMax = u_uvMax;
  	v_repeat = u_repeat;
    gl_Position = u_projectionViewMatrix * a_position;
} 