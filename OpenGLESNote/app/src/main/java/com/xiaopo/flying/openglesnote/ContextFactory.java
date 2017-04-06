package com.xiaopo.flying.openglesnote;

import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * @author wupanjie
 */

public class ContextFactory implements GLSurfaceView.EGLContextFactory {
  private static int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
  private static double glVersion = 3.0;

  public EGLContext createContext(
      EGL10 egl, EGLDisplay display, EGLConfig eglConfig) {

    int[] attrib_list = {EGL_CONTEXT_CLIENT_VERSION, (int) glVersion,
        EGL10.EGL_NONE };
    // attempt to create a OpenGL ES 3.0 context
    EGLContext context = egl.eglCreateContext(
        display, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list);
    return context; // returns null if 3.0 is not supported;
  }

  @Override public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {

  }
}
