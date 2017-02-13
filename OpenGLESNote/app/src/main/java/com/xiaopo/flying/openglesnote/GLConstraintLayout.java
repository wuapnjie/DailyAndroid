package com.xiaopo.flying.openglesnote;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author wupanjie
 */

public class GLConstraintLayout extends ConstraintLayout {
  private static final String TAG = "GLConstraintLayout";

  public GLConstraintLayout(Context context) {
    super(context);
  }

  public GLConstraintLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public GLConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    Log.d(TAG, "onSizeChanged: " + w + "," + h);
  }

}
