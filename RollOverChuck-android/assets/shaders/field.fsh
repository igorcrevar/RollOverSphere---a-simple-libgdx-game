#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP
#endif

varying vec2 v_texCoords;
varying vec2 v_repeat;
varying vec2 v_uvMin;
varying vec2 v_uvMax;

uniform sampler2D u_texture;

void main()
{
	vec2 tmp = v_texCoords * v_repeat;
	tmp = fract(tmp) * (v_uvMax - v_uvMin) + v_uvMin;
	gl_FragColor = texture2D(u_texture, tmp);
}