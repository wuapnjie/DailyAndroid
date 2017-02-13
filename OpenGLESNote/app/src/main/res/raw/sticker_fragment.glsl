varying vec2 vTexCoord;
uniform sampler2D uStickerTexture;

void main() {
  gl_FragColor = texture2D(uStickerTexture,vTexCoord);
}
