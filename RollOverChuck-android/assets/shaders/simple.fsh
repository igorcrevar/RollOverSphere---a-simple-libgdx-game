#ifdef GL_ES
	// Set the default precision to medium. We don't need as high of a precision in the fragment shader.
	#define LOWP lowp
	precision mediump float; 
#else
	#define LOWP
#endif

varying vec4 v_color;     // This is the color from the vertex shader interpolated across the triangle per fragment.
void main()
{                    
	gl_FragColor = v_color;  // Pass the color directly through the pipeline.
}                