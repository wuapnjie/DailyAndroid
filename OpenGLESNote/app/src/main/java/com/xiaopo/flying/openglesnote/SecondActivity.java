package com.xiaopo.flying.openglesnote;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import com.xiaopo.flying.openglesnote.renderer.MultiSquareRenderer;
import com.xiaopo.flying.openglesnote.renderer.SimpleTextureRenderer;
import com.xiaopo.flying.openglesnote.renderer.StickerRenderer;

public class SecondActivity extends AppCompatActivity {
  private GLSurfaceView glSurfaceView;
  private GLConstraintLayout constraintLayout;
  private ConstraintSet resetConstraintSet = new ConstraintSet();
  private ConstraintSet transformConstraintSet = new ConstraintSet();

  private int deviceWidth;
  private int deviceHeight;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    glSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);

    glSurfaceView.setEGLContextClientVersion(2);
    //glSurfaceView.setEGLContextFactory(new ContextFactory());
    glSurfaceView.setRenderer(new SimpleTextureRenderer(getResources(), R.drawable.niconico));
    //glSurfaceView.setRenderer(new StickerRenderer(getResources()));
    glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    constraintLayout = (GLConstraintLayout) findViewById(R.id.constraintLayout);

    resetConstraintSet.clone(constraintLayout);
    transformConstraintSet.clone(constraintLayout);

    deviceWidth = DipPixelUtil.getDeviceWidth(this);
    deviceHeight = DipPixelUtil.getDeviceHeight(this);

    transformConstraintSet.constrainHeight(R.id.glSurfaceView, 0);
    transformConstraintSet.constrainWidth(R.id.glSurfaceView, deviceWidth);
    transformConstraintSet.centerVertically(R.id.glSurfaceView, R.id.constraintLayout);
    transformConstraintSet.setDimensionRatio(R.id.glSurfaceView, "580:580");
    transformConstraintSet.applyTo(constraintLayout);
  }

  @Override protected void onResume() {
    super.onResume();
  }
}
