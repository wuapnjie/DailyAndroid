package com.xiaopo.flying.scrollernote;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * @author wupanjie
 */

public class PagerLinearLayout extends LinearLayout {
  public static final String TAG = PagerLinearLayout.class.getSimpleName();

  public static final int MODE_SCROLL = 0;
  public static final int MODE_PAGER = 1;

  private Scroller scroller;
  private int touchSlop;
  private float downX;
  private float downY;
  private int lastMoveX;
  private Adapter adapter;

  private int rightBorder;
  private int leftBorder;

  private int mode = MODE_SCROLL;
  private int currentPager = 0;
  private int currentScroll;
  private int currentHeight;
  private boolean needZoom;

  private int minHeight;
  private int maxHeight;

  private VelocityTracker velocityTracker;

  private Interpolator interpolator;

  public PagerLinearLayout(Context context) {
    this(context, null);
  }

  public PagerLinearLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PagerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setOrientation(HORIZONTAL);

    interpolator = new DecelerateInterpolator();

    scroller = new Scroller(context, interpolator);
    ViewConfiguration configuration = ViewConfiguration.get(context);
    touchSlop = configuration.getScaledTouchSlop();
    velocityTracker = VelocityTracker.obtain();
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    if (getChildCount() != 0) {
      leftBorder = getChildAt(0).getLeft();
      rightBorder = getChildAt(getChildCount() - 1).getRight();
    }
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent event) {
    final int action = MotionEventCompat.getActionMasked(event);
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        downX = event.getX();
        downY = event.getY();
        lastMoveX = (int) event.getX();
        scroller.forceFinished(true);
        invalidate();
        currentScroll = getScrollX();

        break;
      case MotionEvent.ACTION_MOVE:
        lastMoveX = (int) event.getX();
        if (Math.abs(downX - event.getX()) >= touchSlop
            || Math.abs(downY - event.getY()) >= touchSlop) {
          return true;
        }
        break;
    }
    return super.onInterceptTouchEvent(event);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (adapter == null) return super.onTouchEvent(event);
    velocityTracker.addMovement(event);

    final int action = MotionEventCompat.getActionMasked(event);

    switch (action) {
      case MotionEvent.ACTION_DOWN:
        downX = event.getX();
        downY = event.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        if (Math.abs(event.getY() - downY) > Math.abs(event.getX() - downX)) {
          needZoom = true;
        }
        break;
    }

    if (needZoom) {
      handleZoom(event);
    } else {
      switch (mode) {
        case MODE_SCROLL:
          return handleScroll(event);
        case MODE_PAGER:
          return handlePager(event);
        default:
          return super.onTouchEvent(event);
      }
    }
    return true;
  }

  private void handleZoom(MotionEvent event) {
    final int action = event.getAction();
    switch (action) {
      case MotionEvent.ACTION_MOVE:
        int height = 0;
        switch (mode) {
          case MODE_PAGER:
            height = maxHeight;
            break;
          case MODE_SCROLL:
            height = minHeight;
            break;
        }
        int scaledSize = (int) ((height + event.getY() - downY) / height * height);
        if (scaledSize < minHeight) {
          scaledSize = minHeight;
        }

        if (scaledSize > maxHeight) {
          scaledSize = maxHeight;
        }

        currentHeight = scaledSize;
        scaleLayout(scaledSize);
        break;

      case MotionEvent.ACTION_UP:
        velocityTracker.computeCurrentVelocity(1000);

        switch (mode) {
          case MODE_PAGER:
            if (currentHeight < (minHeight + maxHeight) / 2
                || velocityTracker.getYVelocity() < -3000) {
              quickScaleLayout(MODE_SCROLL);
            } else {
              quickScaleLayout(MODE_PAGER);
            }
            break;

          case MODE_SCROLL:
            if (currentHeight > (minHeight + maxHeight) / 2
                || velocityTracker.getYVelocity() > 3000) {
              quickScaleLayout(MODE_PAGER);
            } else {
              quickScaleLayout(MODE_SCROLL);
            }
            break;
        }

        needZoom = false;
        break;
    }
  }

  private void quickScaleLayout(final int dstMode) {
    switch (dstMode) {
      case MODE_PAGER:
        quickScaleLayout(maxHeight, MODE_PAGER);
        break;
      case MODE_SCROLL:
        quickScaleLayout(minHeight, MODE_SCROLL);
        break;
    }
  }

  private void quickScaleLayout(int dstHeight, final int dstMode) {
    Log.d(TAG, "quickScaleLayout: dstHeight-->" + dstHeight);
    ValueAnimator animator = ValueAnimator.ofInt(currentHeight, dstHeight);
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        currentHeight = (int) animation.getAnimatedValue();
        scaleLayout(currentHeight);
      }
    });

    animator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        if (mode == MODE_SCROLL && dstMode == MODE_PAGER) {
          currentPager = Math.round((float) (getScrollX()) / maxHeight);
          int dx = currentPager * maxHeight - getScrollX() - (getWidth() - maxHeight) / 2;
          scroller.startScroll(getScrollX(), 0, dx, 0);
        }
        mode = dstMode;
      }
    });

    animator.setInterpolator(interpolator);
    animator.setDuration(300);
    animator.start();
  }

  private void scaleLayout(int scaledSize) {
    for (int i = 0; i < getChildCount(); i++) {
      View view = getChildAt(i);
      ViewGroup.LayoutParams params = view.getLayoutParams();
      params.height = scaledSize;
      params.width = scaledSize;

      if (i == 0) {
        view.setPadding(currentHeight / 15, 0, currentHeight / 30, 0);
      } else if (i == getChildCount()) {
        view.setPadding(currentHeight / 30, 0, currentHeight / 15, 0);
      } else {
        view.setPadding(currentHeight / 30, 0, currentHeight / 30, 0);
      }
    }
    ViewGroup.LayoutParams layoutParams = getLayoutParams();
    layoutParams.height = scaledSize;

    requestLayout();

    if (mode == MODE_PAGER) {
      int scroll = currentPager * scaledSize - (getWidth() - scaledSize) / 2;
      if (currentPager == getChildCount() - 1) scroll = rightBorder - getWidth();
      if (scroll < 0) scroll = 0;
      if (scroll >= rightBorder - getWidth()) {
        scroller.startScroll(getScrollX(), 0, scroll - getScrollX(), 0);
      } else {
        //if (scroll < (int) (currentScroll * ((float) scaledSize / maxHeight))) {
        //  scroll = (int) (currentScroll * ((float) scaledSize / maxHeight));
        //}
        scrollTo(scroll, 0);
      }
    } else {
      scrollTo((int) (currentScroll * ((float) scaledSize / minHeight)), 0);
    }
  }

  private boolean handlePager(MotionEvent event) {
    final int action = MotionEventCompat.getActionMasked(event);
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        scroller.forceFinished(true);
        invalidate();
        downX = event.getX();
        lastMoveX = (int) event.getX();
        break;
      case MotionEvent.ACTION_MOVE:
        int scrollX = (int) (lastMoveX - event.getRawX());
        if (getScrollX() + scrollX < leftBorder) {
          scrollTo(leftBorder, 0);
          return true;
        }
        if (getScrollX() + scrollX + getWidth() > rightBorder) {
          scrollTo(rightBorder - getWidth(), 0);
          return true;
        }
        scrollBy(scrollX, 0);
        lastMoveX = (int) event.getRawX();
        break;

      case MotionEvent.ACTION_UP:
        velocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) Math.abs(velocityTracker.getXVelocity());

        final float finalX = event.getX();
        View view = getChildAt(currentPager);
        int width = view.getWidth();

        if (finalX < downX) {
          if ((downX - finalX) > width / 3 || velocity > 3000) currentPager++;
        } else {
          if ((finalX - downX) > width / 3 || velocity > 3000) currentPager--;
        }
        if (currentPager < 0) currentPager = 0;
        if (currentPager >= adapter.getItemCount()) currentPager = getChildCount() - 1;

        int dx = currentPager * width - getScrollX() - (getWidth() - width) / 2;
        scroller.startScroll(getScrollX(), 0, dx, 0);
        invalidate();
        velocityTracker.clear();
        break;
    }
    return true;
  }

  private boolean handleScroll(MotionEvent event) {
    final int action = MotionEventCompat.getActionMasked(event);
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        scroller.forceFinished(true);
        invalidate();
        lastMoveX = (int) event.getX();
        break;
      case MotionEvent.ACTION_MOVE:
        int scrollX = (int) (lastMoveX - event.getRawX());
        if (getScrollX() + scrollX < leftBorder) {
          scrollTo(leftBorder, 0);
          return true;
        }
        if (getScrollX() + scrollX + getWidth() > rightBorder) {
          scrollTo(rightBorder - getWidth(), 0);
          return true;
        }
        scrollBy(scrollX, 0);
        lastMoveX = (int) event.getRawX();
        break;
      case MotionEvent.ACTION_UP:
        velocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) velocityTracker.getXVelocity();
        scroller.fling(getScrollX(), getScrollY(), -velocity, 0, -rightBorder, rightBorder, 0, 0);
        invalidate();
        velocityTracker.clear();
        break;
    }
    return true;
  }

  public void setAdapter(Adapter adapter) {
    if (adapter == null) return;
    this.adapter = adapter;
    switch (mode) {
      case MODE_SCROLL:
        currentHeight = minHeight;
        break;
      case MODE_PAGER:
        currentHeight = maxHeight;
        break;
    }

    removeAllViews();

    for (int i = 0; i < adapter.getItemCount(); i++) {
      View view = adapter.createView(this);
      if (i == 0) {
        view.setPadding(currentHeight / 15, 0, currentHeight / 30, 0);
      } else if (i == getChildCount()) {
        view.setPadding(currentHeight / 30, 0, currentHeight / 15, 0);
      } else {
        view.setPadding(currentHeight / 30, 0, currentHeight / 30, 0);
      }
      addView(view);
      adapter.bindView(view, getChildCount() - 1);
    }

    requestLayout();
  }

  @Override public void computeScroll() {
    super.computeScroll();
    if (scroller.computeScrollOffset()) {
      scrollTo(scroller.getCurrX(), scroller.getCurrY());
      if (scroller.getCurrX() < leftBorder) {
        scroller.forceFinished(true);
        scrollTo(leftBorder, 0);
      } else if (scroller.getCurrX() + getWidth() > rightBorder) {
        scroller.forceFinished(true);
        scrollTo(rightBorder - getWidth(), 0);
      }
      invalidate();
    }
  }

  @Override protected void onDetachedFromWindow() {
    velocityTracker.recycle();
    super.onDetachedFromWindow();
  }

  public void setMode(int mode) {
    if (this.mode == mode) return;
    quickScaleLayout(maxHeight, mode);
  }

  public int getMinHeight() {
    return minHeight;
  }

  public void setMinHeight(int minHeight) {
    this.minHeight = minHeight;
    quickScaleLayout(mode);
  }

  public void setMaxHeight(int maxHeight) {
    this.maxHeight = maxHeight;
    quickScaleLayout(mode);
  }

  public static abstract class Adapter {

    public abstract View createView(ViewGroup parent);

    public abstract void bindView(View view, int position);

    public abstract int getItemCount();
  }
}
