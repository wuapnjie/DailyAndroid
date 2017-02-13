package com.xiaopo.flying.openglesnote.renderer;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import com.xiaopo.flying.openglesnote.R;
import com.xiaopo.flying.openglesnote.ShaderUtil;
import com.xiaopo.flying.openglesnote.TextureUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

/**
 * @author wupanjie
 */

public class MultiSquareRenderer implements GLSurfaceView.Renderer {
  private static final float[] VERTEX_ONE = {
      0f, 0f, 0f, 1f, 0f, 0f, 1f, 1f, 0f, 0f, 1f, 0f
  };

  private static final float[] VERTEX_TWO = {
      0f, 0f, -1f, 0f, -1f, -1f, 0f, -1f
  };

  private static final short[] VERTEX_INDEX = { 0, 1, 2, 0, 2, 3 };

  private static final float[] TEX_VERTEX = {
      1f, 1f,  // top right
      0f, 1f,  // top left
      0f, 0,  // bottom left
      1f, 0,  // bottom right
  };

  private final Resources resources;
  private int programId;
  private String[] attributes = new String[] { "aVertexPosition", "aTexCoord" };
  private final FloatBuffer vertexBufferOne;
  private final FloatBuffer vertexBufferTwo;
  private final FloatBuffer vertexTextureBuffer;
  private final ShortBuffer vertexIndexBuffer;

  private int textureHandleOne;
  private int textureHandleTwo;
  private int textureUniformHandle;

  public MultiSquareRenderer(Resources resources) {
    this.resources = resources;

    vertexBufferOne = ByteBuffer.allocateDirect(VERTEX_ONE.length * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(VERTEX_ONE);
    vertexBufferOne.position(0);

    vertexBufferTwo = ByteBuffer.allocateDirect(VERTEX_TWO.length * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(VERTEX_TWO);
    vertexBufferTwo.position(0);

    vertexTextureBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.length * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(TEX_VERTEX);
    vertexTextureBuffer.position(0);

    vertexIndexBuffer = ByteBuffer.allocateDirect(VERTEX_INDEX.length * 2)
        .order(ByteOrder.nativeOrder())
        .asShortBuffer()
        .put(VERTEX_INDEX);
    vertexIndexBuffer.position(0);
  }

  @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

  }

  @Override public void onSurfaceChanged(GL10 gl10, int i, int i1) {
    String vertexCode = ShaderUtil.glslToString(resources, R.raw.multi_square_vertex);
    String fragmentCode = ShaderUtil.glslToString(resources, R.raw.multi_square_fragment);
    final int vertexHandle = ShaderUtil.loadShader(GL_VERTEX_SHADER, vertexCode);
    final int fragmentHandle = ShaderUtil.loadShader(GL_FRAGMENT_SHADER, fragmentCode);

    textureHandleOne = TextureUtil.loadTextureFromResource(resources, R.drawable.niconico);
    textureHandleTwo = TextureUtil.loadTextureFromResource(resources, R.drawable.pppp1);

    textureUniformHandle = glGetUniformLocation(programId, "uTexture");

    programId = ShaderUtil.createAndLinkProgram(vertexHandle, fragmentHandle, attributes);
  }

  @Override public void onDrawFrame(GL10 gl10) {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glUseProgram(programId);

    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, vertexBufferOne);

    glEnableVertexAttribArray(1);
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, vertexTextureBuffer);

    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, textureHandleOne);
    glUniform1i(textureUniformHandle, 0);

    //glDrawArrays(GL_TRIANGLES, 0, 3);
    glDrawElements(GL_TRIANGLES, VERTEX_INDEX.length, GL_UNSIGNED_SHORT, vertexIndexBuffer);

    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);

    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, vertexBufferTwo);

    glEnableVertexAttribArray(1);
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, vertexTextureBuffer);

    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, textureHandleTwo);
    glUniform1i(textureUniformHandle, 0);

    //glDrawArrays(GL_TRIANGLES, 0, 3);
    glDrawElements(GL_TRIANGLES, VERTEX_INDEX.length, GL_UNSIGNED_SHORT, vertexIndexBuffer);
  }
}
