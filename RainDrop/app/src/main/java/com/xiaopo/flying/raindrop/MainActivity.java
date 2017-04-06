package com.xiaopo.flying.raindrop;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private GLSurfaceView glSurfaceView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
    glSurfaceView.setEGLContextClientVersion(2);
    glSurfaceView.setRenderer(new RainDropRenderer(getResources()));
    glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

  }
}
