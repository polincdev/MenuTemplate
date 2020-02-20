  #import "Common/ShaderLib/GLSLCompat.glsllib"
 
uniform vec2 m_Resolution;
 
uniform float m_Strength;
 
uniform sampler2D m_Texture;
varying vec2 texCoord;

  vec4 boxblur(  sampler2D tex,     vec2 uv,     vec2 resolution)
{
	  vec2 r = 1.0 / resolution;
    
    const   float off = 3.0;
    const   float v = off * 2.0 + 1.0;
    const   float d = 1.0 / (v * v);

      vec4 color = vec4(0.0);
    for (float x = -off; x <= off; x++)
    {
        for (float y = -off; y <= off; y++)
        {
              vec2 coord = vec2(uv.x + x * r.x, uv.y + y * r.y);
            color += texture2D(tex, coord) * d;
        }
    }
        
    return color;
}

void main() {
  
     vec2 uv = texCoord ;
       vec4 col = boxblur(m_Texture, uv, m_Resolution.xy);
     gl_FragColor =col;
}
