#ifdef GL_ES
	// Set the default precision to medium. We don't need as high of a precision in the fragment shader.
	#define LOWP lowp
	precision mediump float; 
#else
	#define LOWP
#endif

void main()
{                    
	gl_FragColor = vec4(0, 0, 0, 0.2); 
}                