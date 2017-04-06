package com.xiaopo.flying.raindrop;

/**
 * @author wupanjie
 */

import android.annotation.SuppressLint;
import android.content.Context;

public class DipPixelUtil {
  private static float sScale = 0;

  public static int getDeviceWidth(Context context) {
     return context.getResources().getDisplayMetrics().widthPixels;
  }

  public static int getDeviceHeight(Context context) {
    return context.getResources().getDisplayMetrics().heightPixels;
  }

  @SuppressLint("DefaultLocale") public static int dip2px(Context context, float dpValue) {
    if (sScale == 0) {
      sScale = context.getResources().getDisplayMetrics().density;
    }
    return (int) (dpValue * sScale + 0.5f);
  }

  public static int px2dip(Context context, float pxValue) {
    if (sScale == 0) {
      sScale = context.getResources().getDisplayMetrics().density;
    }
    return (int) (pxValue / sScale + 0.5f);
  }

  public static int sp2pix(float spValue, float fontScale) {
    return (int) (spValue * fontScale + 0.5f);
  }

  public static int sp2px(Context context, float spValue) {
    final float scale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (spValue * scale);
  }
}

