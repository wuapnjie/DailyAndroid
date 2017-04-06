package com.xiaopo.flying.raindrop;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindAttribLocation;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;

/**
 * @author wupanjie
 */

public class ShaderUtil {
  private static final String TAG = "ShaderUtil";

  public static int loadShader(int type, String shaderCode) {
    int shaderHandle = glCreateShader(type);
    if (shaderHandle != 0) {
      //传递着色代码
      glShaderSource(shaderHandle, shaderCode);
      //编译着色代码
      glCompileShader(shaderHandle);

      //获取编译状态
      final int[] compileStatus = new int[1];
      glGetShaderiv(shaderHandle, GL_COMPILE_STATUS, compileStatus, 0);

      //编译失败
      if (compileStatus[0] == 0) {
        Log.e(TAG, "loadShader: Error compiling shader: " + glGetShaderInfoLog(shaderHandle));
        glDeleteShader(shaderHandle);
        shaderHandle = 0;
      }
    }

    if (shaderHandle == 0) {
      throw new RuntimeException("Error creating shader");
    }

    return shaderHandle;
  }

  public static int createAndLinkProgram(final int vertexShaderHandle,
      final int fragmentShaderHandle, final String[] attributes) {
    //创建程序
    int programHandle = glCreateProgram();

    if (programHandle != 0) {
      //绑定顶点着色器
      glAttachShader(programHandle, vertexShaderHandle);

      //绑定片段着色器
      glAttachShader(programHandle, fragmentShaderHandle);

      //可选，为顶点着色器中每个attribute变量绑定位置,若不绑定，可在程序Link后通过glGetAttributeLocation方法获取每个变量的位置
      if (attributes != null) {
        for (int i = 0; i < attributes.length; i++) {
          glBindAttribLocation(programHandle, i, attributes[i]);
        }
      }

      //链接程序
      glLinkProgram(programHandle);

      //获取链接状态
      final int[] linkStatus = new int[1];
      glGetProgramiv(programHandle, GL_LINK_STATUS, linkStatus, 0);

      //链接失败
      if (linkStatus[0] == 0) {
        Log.e(TAG, "createAndLinkProgram : Error compiling program: " + GLES20.glGetProgramInfoLog(
            programHandle));

        //删除程序
        glDeleteProgram(programHandle);
        programHandle = 0;
      }
    }

    if (programHandle == 0) {
      throw new RuntimeException("Error creating program");
    }

    return programHandle;
  }

  public static String glslToString(Resources resources, int resId) {
    InputStream inputStream = resources.openRawResource(resId);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

    StringBuilder stringBuilder = new StringBuilder();

    String line;
    try {
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return stringBuilder.toString();
  }
}
