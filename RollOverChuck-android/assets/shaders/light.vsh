attribute vec3 a_position;
attribute vec4 a_color;
attribute vec3 a_normal;

uniform mat4 u_projectionViewMatrix;
uniform mat4 u_viewModelMatrix;
uniform vec3 u_lightPos;
uniform vec4 u_color_factor;

// This will be passed into the fragment shader.
varying vec4 v_color;  
 
void main()
{          
	// Transform the vertex into eye space.
	vec3 modelViewVertex = vec3(u_viewModelMatrix * vec4(a_position, 0));
	// Transform the normal's orientation into eye space.
	vec3 modelViewNormal = vec3(u_viewModelMatrix * vec4(a_normal, 0.0));
	// Will be used for attenuation.
	vec3 directionObjectLight = u_lightPos - modelViewVertex;
	float distance = length(directionObjectLight);        
	// Get a lighting direction vector from the light to the vertex.
	vec3 lightVector = normalize(directionObjectLight);   
	// Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
	// pointing in the same direction then it will get max illumination.
	float diffuse = max(dot(modelViewNormal, lightVector), 0.1); 
	// Attenuate the light based on distance.
	diffuse = diffuse * (1.0 / (1.0 + (0.000002 * distance * distance))); 
	// Multiply the color by the illumination level. It will be interpolated across the triangle.
	v_color = a_color * diffuse * u_color_factor;
	v_color.w = a_color.w * u_color_factor.w;
	// gl_Position is a special variable used to store the final position.
	// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
   	gl_Position = u_projectionViewMatrix * vec4(a_position, 1);
}