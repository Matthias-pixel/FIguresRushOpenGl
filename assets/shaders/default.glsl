#type vertex
#version 330 core
uniform mat4 uProjection;
uniform mat4 uView;
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexID;
out vec4 fColor;
out vec2 fTexCoords;
out float fTexID;


void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexID = aTexID;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexID;
uniform sampler2D uTextures[8];
out vec4 color;

void main()
{
    /*if(fTexID == 0)
        color = vec4(1, 0, 0, 1);
    else if(fTexID == 1)
        color = vec4(0, 1, 0, 1);
    else if(fTexID == 2)
        color = vec4(0, 0, 1, 1);
    else
        color = vec4(1, 1, 1, 1);*/
    if(fTexID > 0) {
        int id = int(fTexID);
        color = fColor*texture(uTextures[id], fTexCoords);
    } else {
        color = fColor;
    }
}