uniform mat4 uMVPMatrix;

attribute vec4 vPosition;
attribute vec2 a_texCoord;

varying vec2 v_texCoord;
varying vec4 v_vertexCoord;

void main() {
  //gl_Position = uMVPMatrix * vPosition;
  gl_Position = vPosition;
  v_texCoord = a_texCoord;
  v_vertexCoord = vPosition;
}