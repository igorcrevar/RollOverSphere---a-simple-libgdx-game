attribute vec4 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform vec2 u_uvMin;
uniform vec2 u_uvMax;

varying vec2 v_texCoords;
varying vec4 v_uvMinMax;

void main()
{
  	v_texCoords = a_texCoord0 * (u_uvMax - u_uvMin) + u_uvMin;
    gl_Position = u_projTrans * a_position;
}