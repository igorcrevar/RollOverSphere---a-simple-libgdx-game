attribute vec3 a_position;

uniform mat4 u_projectionViewMatrix;
uniform mat4 u_modelMatrix;
uniform vec3 u_lightPos;
uniform float u_planeY;
 
void main()
{          
	// convert to world space
	vec3 convPos = vec3(u_modelMatrix * vec4(a_position, 1)); 
	
	// direction from light to vertex 
	vec3 dir = normalize(convPos - u_lightPos);
	
	// plane y is known (u_planeY) --  its x/z plane
	float t = (-u_lightPos.y + u_planeY) / dir.y;
	
	vec4 proj = vec4(t * dir.x + u_lightPos.x, u_planeY, t * dir.z + u_lightPos.z, 1);
	
   	gl_Position = u_projectionViewMatrix * proj;
}