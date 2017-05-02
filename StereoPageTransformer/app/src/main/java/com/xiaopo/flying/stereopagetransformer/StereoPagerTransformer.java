package com.xiaopo.flying.stereopagetransformer;

import android.support.v4.view.ViewPager;
import android.view.View;

import static java.lang.Math.pow;

/**
 * @author wupanjie
 */

public class StereoPagerTransformer implements ViewPager.PageTransformer {
    private static final String TAG = "StereoPagerTransformer";
    private static final float MAX_ROTATE_Y = 90;
    private final float pageWidth;
    private float factor = 1f;

    public StereoPagerTransformer(float pageWidth) {
        this.pageWidth = pageWidth;
    }

    public void transformPage(View view, float position) {
        view.setPivotY(view.getHeight() / 2);
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setPivotX(0);
            view.setRotationY(90);
        } else if (position <= 0) { // [-1,0]
            view.setPivotX(pageWidth);
            if (position > -0.7f) {
                view.setRotationY(position * (float) pow(0.7, 3) * MAX_ROTATE_Y);
            } else {
                view.setRotationY((float) (-pow(-position, 4) * MAX_ROTATE_Y));
            }
        } else if (position <= 1) { // (0,1]
            view.setPivotX(0);
            if (position < 0.7f) {
                view.setRotationY(position * (float) pow(0.7, 3) * MAX_ROTATE_Y);
            } else {
                view.setRotationY((float) (pow(position, 4) * MAX_ROTATE_Y));
            }
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setPivotX(0);
            view.setRotationY(90);
        }
    }
}
