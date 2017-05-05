package com.xiaopo.flying.stereopagetransformer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author wupanjie
 */

public class OverViewPager extends ViewPager {
    private float lastMotionX;
    private int currentPosition;
    private OnOverScrollListener onOverScrollListener;

    public OverViewPager(Context context) {
        this(context, null);
    }

    public OverViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
            }
        });
    }

    public void setOnOverScrollListener(OnOverScrollListener onOverScrollListener) {
        this.onOverScrollListener = onOverScrollListener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getAdapter() == null) {
            return super.onTouchEvent(event);
        }

        final int action = event.getAction() & MotionEventCompat.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastMotionX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                final float deltaX = lastMotionX - x;
                lastMotionX = x;

                float oldScrollX = getScrollX();
                float scrollX = oldScrollX + deltaX;

                final int width = getPagerWidth();

                float leftBound = 0;
                float rightBound = width * (getAdapter().getCount() - 1);

                if (scrollX < leftBound) {
                    if (currentPosition == 0) {
                        float over = leftBound - scrollX;
                        if (onOverScrollListener != null) {
                            onOverScrollListener.onOverScroll(over);
                        }
                    }
                    scrollX = leftBound;
                } else if (scrollX > rightBound) {
                    if (currentPosition == getAdapter().getCount() - 1) {
                        float over = scrollX - rightBound;
                        if (onOverScrollListener != null) {
                            onOverScrollListener.onOverScroll(-over);
                        }
                    }
                    scrollX = rightBound;
                }
                lastMotionX += scrollX - (int) scrollX;
                break;
        }

        return super.onTouchEvent(event);
    }

    private int getPagerWidth() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    public interface OnOverScrollListener {
        void onOverScroll(float overScroll);
    }
}