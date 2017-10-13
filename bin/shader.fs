#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 colour;
uniform int useColour;
uniform int selected;

void main()
{
    if ( useColour == 1 )
    {
        fragColor = vec4(colour, 1);
    }
    else
    {
        fragColor = texture(texture_sampler, outTexCoord);
    }
    
    if ( selected == 1) 
    {
		fragColor = vec4(0.0, 0.0, 0.3, 0.3);
	}
	else 
	{
	     fragColor = texture(texture_sampler, outTexCoord);
	}
}