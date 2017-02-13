package com.xiaopo.flying.openglesnote.renderer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
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
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * @author wupanjie
 */

public class SimpleTextureRenderer implements GLSurfaceView.Renderer {
  private static final float[] VERTEX = {   // in counterclockwise order:
      1, 1, 0f, // top right
      -1f, 1f, 0f, // top left
      -1f, -1f, 0f,  // bottom left
      1f, -1f, 0f,  //bottom right
  };

  private static final short[] VERTEX_INDEX = { 0, 1, 2, 0, 2, 3 };

  //private static final float[] UV_TEX_VERTEX = {
  //    0.8f, 0,  // bottom right
  //    0.2f, 0,  // bottom left
  //    0.2f, 1f,  // top left
  //    0.8f, 1f,  // top right
  //};
  private static final float[] UV_TEX_VERTEX = {
      1f, 0,  // bottom right
      0f, 0,  // bottom left
      0f, 1f,  // top left
      1f, 1f,  // top right
  };
  private final FloatBuffer vertexBuffer;
  private final ShortBuffer vertexIndexBuffer;
  private final FloatBuffer uvTexVertexBuffer;

  private int program;
  private int positionHandle;
  private int matrixHandle;
  private int texCoordHandle;
  private int textSampleHandle;

  private final float[] projectionMatrix = new float[16];
  private final float[] cameraMatrix = new float[16];
  private final float[] mvpMatrix = new float[16];

  private final Resources resources;
  private final int resId;

  public SimpleTextureRenderer(Resources resources, int resId) {
    this.resources = resources;
    this.resId = resId;
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

    uvTexVertexBuffer = ByteBuffer.allocateDirect(UV_TEX_VERTEX.length * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(UV_TEX_VERTEX);
    uvTexVertexBuffer.position(0);
  }

  @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {

  }

  @Override public void onSurfaceChanged(GL10 gl, int width, int height) {
    //program = glCreateProgram();
    String vertexShaderCode = ShaderUtil.glslToString(resources, R.raw.my_vertex);
    String fragmentShaderCode = ShaderUtil.glslToString(resources, R.raw.my_fragment);
    int vertexShader = ShaderUtil.loadShader(GL_VERTEX_SHADER, vertexShaderCode);
    int fragmentShader = ShaderUtil.loadShader(GL_FRAGMENT_SHADER, fragmentShaderCode);

    program = ShaderUtil.createAndLinkProgram(vertexShader, fragmentShader, null);

    positionHandle = glGetAttribLocation(program, "vPosition");
    matrixHandle = glGetUniformLocation(program, "uMVPMatrix");
    texCoordHandle = glGetAttribLocation(program, "a_texCoord");
    textSampleHandle = glGetUniformLocation(program, "s_texture");

    Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);
    glActiveTexture(GL_TEXTURE0);
    TextureUtil.loadTextureFromBitmap(bitmap, true);

    //x坐标匹配
    float ratio = (float) height / width;
    Matrix.frustumM(projectionMatrix, 0, -1, 1, -ratio, ratio, 3, 7);

    //y坐标匹配
    //float radio = (float) width/height;
    //Matrix.frustumM(projectionMatrix,0,-radio,radio,-1,1,3,7);

    Matrix.setLookAtM(cameraMatrix, 0, 0, 0, 3, 0, 0, 0, 0, 1, 0);
    Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, cameraMatrix, 0);
  }

  @Override public void onDrawFrame(GL10 gl) {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    glUseProgram(program);
    glEnableVertexAttribArray(positionHandle);
    glVertexAttribPointer(positionHandle, 3, GL_FLOAT, false, 0, vertexBuffer);

    glEnableVertexAttribArray(texCoordHandle);
    glVertexAttribPointer(texCoordHandle, 2, GL_FLOAT, false, 0, uvTexVertexBuffer);

    glUniformMatrix4fv(matrixHandle, 1, false, mvpMatrix, 0);
    glUniform1i(textSampleHandle, 0);

    glDrawElements(GL_TRIANGLES, VERTEX_INDEX.length, GL_UNSIGNED_SHORT, vertexIndexBuffer);

    glDisableVertexAttribArray(positionHandle);
    glDisableVertexAttribArray(texCoordHandle);
  }
}
