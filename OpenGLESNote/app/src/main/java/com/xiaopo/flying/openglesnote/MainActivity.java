package com.xiaopo.flying.openglesnote;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    GLSurfaceView glSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);

    glSurfaceView.setEGLContextClientVersion(2);
    glSurfaceView.setRenderer(new MyRenderer());
    glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
  }

  static class MyRenderer implements GLSurfaceView.Renderer {
    private static final String VERTEX_SHADER = "attribute vec4 vPosition;\n"
        + "uniform mat4 uMVPMatrix;\n"
        + "void main() {\n"
        + "  gl_Position = uMVPMatrix * vPosition;\n"
        + "}";
    private static final String FRAGMENT_SHADER = "precision mediump float;\n"
        + "void main() {\n"
        + "  gl_FragColor = vec4(0.5,0,0,1);\n"
        + "}";
    private static final float[] VERTEX = {   // in counterclockwise order:
        0, 1, 0.0f, // top
        -0.5f, -1, 0.0f, // bottom left
        1f, -1, 0.0f,  // bottom right
    };

    private final FloatBuffer vertexBuffer;

    private int program;
    private int positionHandle;
    private int matrixHandle;

    private final float[] projectionMatrix = new float[16];
    private final float[] cameraMatrix = new float[16];
    private final float[] mvpMatrix = new float[16];

    MyRenderer() {
      vertexBuffer = ByteBuffer.allocateDirect(VERTEX.length * 4)
          .order(ByteOrder.nativeOrder())
          .asFloatBuffer()
          .put(VERTEX);

      vertexBuffer.position(0);
    }

    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height) {
      program = glCreateProgram();
      int vertexShader = loadShader(GL_VERTEX_SHADER, VERTEX_SHADER);
      int fragmentShader = loadShader(GL_FRAGMENT_SHADER, FRAGMENT_SHADER);
      glAttachShader(program, vertexShader);
      glAttachShader(program, fragmentShader);
      glLinkProgram(program);

      positionHandle = glGetAttribLocation(program, "vPosition");
      matrixHandle = glGetUniformLocation(program,"uMVPMatrix");

      //x坐标匹配
      float ratio = (float) height / width;
      Matrix.frustumM(projectionMatrix, 0, -1, 1, -ratio, ratio, 3, 7);

      ////y坐标匹配
      //float radio = (float) width/height;
      //Matrix.frustumM(projectionMatrix,0,-radio,radio,-1,1,3,7);


      Matrix.setLookAtM(cameraMatrix,0,0,0,3,0,0,0,0,1,0);
      Matrix.multiplyMM(mvpMatrix,0,projectionMatrix,0,cameraMatrix,0);
    }

    @Override public void onDrawFrame(GL10 gl) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

      glUseProgram(program);
      glEnableVertexAttribArray(positionHandle);
      glVertexAttribPointer(positionHandle, 3, GL_FLOAT, false, 12, vertexBuffer);
      glUniformMatrix4fv(matrixHandle,1,false,mvpMatrix,0);

      glDrawArrays(GL_TRIANGLES, 0, 3);
      glDisableVertexAttribArray(positionHandle);
    }

    static int loadShader(int type, String shaderCode) {
      int shader = glCreateShader(type);
      glShaderSource(shader, shaderCode);
      glCompileShader(shader);
      return shader;
    }
  }
}
