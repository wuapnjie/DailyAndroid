attribute vec4 aVertexPosition;
attribute vec2 aTexCoord;
varying vec2 vTexCoord;

void main() {
  vTexCoord = aTexCoord;
  gl_Position = aVertexPosition;
}
