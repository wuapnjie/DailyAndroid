precision mediump float;

uniform sampler2D s_texture;

varying vec2 v_texCoord;
varying vec4 v_vertexCoord;

void main() {
  vec3 textureRGB = texture2D(s_texture,v_texCoord).rgb;
  //vec3 textureRGB =  vec3(1.0,1.0,1.0) - texture2D(s_texture,v_texCoord).rgb;
  //textureRGB.r = 1.0 - textureRGB.r;
  //textureRGB.g = 1.0 - textureRGB.g;
  //textureRGB.b = 1.0 - textureRGB.b;
  if(gl_FragCoord.x < 0.0){
    gl_FragColor = vec4(1.0,0,0 ,1.0);
  }else {
    gl_FragColor = vec4(textureRGB ,1.0);
  }

}
