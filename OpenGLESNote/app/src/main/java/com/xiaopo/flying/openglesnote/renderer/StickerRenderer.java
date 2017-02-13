package com.xiaopo.flying.openglesnote.renderer;

import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import com.xiaopo.flying.openglesnote.R;
import com.xiaopo.flying.openglesnote.ShaderUtil;
import com.xiaopo.flying.openglesnote.TextureUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * @author wupanjie
 */

public class StickerRenderer implements GLSurfaceView.Renderer {
  public static final float[] VERTEX = new float[] {
      -1f, 0f, 0f, -1f, 1f, 0f, 0f, 1f,
  };

  public static final short[] VERTEX_INDEX = new short[] {
      0, 1, 2, 0, 2, 3
  };

  public static final float[] TEXTURE_COORD = new float[] {
      0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f
  };

  private final FloatBuffer vertexBuffer;
  private final ShortBuffer vertexIndexBuffer;
  private final FloatBuffer textureCoordBuffer;

  private final Resources resources;

  private final String[] attributes = { "aVertexPosition", "aTexCoord" };
  private int program;
  private int textureUniformHandle;
  private int textureStickerHandle;

  public StickerRenderer(Resources resources) {
    this.resources = resources;

    vertexBuffer = ByteBuffer.allocateDirect(VERTEX.length * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(VERTEX);
    vertexBuffer.position(0);

    vertexIndexBuffer = ByteBuffer.allocateDirect(VERTEX_INDEX.length * 2)
        .order(ByteOrder.nativeOrder())
        .asShortBuffer()
        .put(VERTEX_INDEX);
    vertexIndexBuffer.position(0);

    textureCoordBuffer = ByteBuffer.allocateDirect(TEXTURE_COORD.length * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(TEXTURE_COORD);
    textureCoordBuffer.position(0);
  }

  @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {

  }

  @Override public void onSurfaceChanged(GL10 gl, int width, int height) {
    String vertexCode = ShaderUtil.glslToString(resources, R.raw.multi_square_vertex);
    String fragmentCode = ShaderUtil.glslToString(resources, R.raw.multi_square_fragment);
    final int vertexHandle = ShaderUtil.loadShader(GL_VERTEX_SHADER, vertexCode);
    final int fragmentHandle = ShaderUtil.loadShader(GL_FRAGMENT_SHADER, fragmentCode);

    program = ShaderUtil.createAndLinkProgram(vertexHandle, fragmentHandle, attributes);

    textureStickerHandle = TextureUtil.loadTextureFromResource(resources, R.drawable.niconico);
    textureUniformHandle = glGetUniformLocation(program, "uStickerTexture");
  }

  @Override public void onDrawFrame(GL10 gl) {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glUseProgram(program);

    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, vertexBuffer);

    glEnableVertexAttribArray(1);
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, textureCoordBuffer);

    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, textureStickerHandle);
    glUniform1i(textureUniformHandle, 0);

    glDrawElements(GL_TRIANGLES, VERTEX_INDEX.length, GL_UNSIGNED_SHORT, vertexIndexBuffer);

    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);
  }
}
