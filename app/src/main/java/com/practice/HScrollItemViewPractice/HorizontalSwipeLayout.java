package com.practice.HScrollItemViewPractice;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by zhuyakun on 2017/11/3.
 */

public class HorizontalSwipeLayout extends FrameLayout {
    ViewDragHelper viewDragHelper;
    int swipeDistance = 0;
    public static final String TAG = HorizontalSwipeLayout.class.getSimpleName();

    public HorizontalSwipeLayout(Context context) {
        super(context);
        init(null);
    }

    public HorizontalSwipeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HorizontalSwipeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    public void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalSwipeLayout);
            swipeDistance = (int) a.getDimension(R.styleable.HorizontalSwipeLayout_swipt_distance, 0);
            a.recycle();
        }
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                try {
                    return child == getChildAt(0);
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return 0;
            }


            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.e(TAG, "clampViewPositionHorizontal:vdhleft" + left);
                return Math.max(-swipeDistance, Math.min(left, 0));
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return 0;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 1;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (-getChildAt(0).getLeft() > swipeDistance * 2 / 3) {
                    viewDragHelper.settleCapturedViewAt(-swipeDistance, 0);
                    postInvalidate();
                } else {
                    viewDragHelper.settleCapturedViewAt(0, 0);
                    postInvalidate();
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams lp = getChildAt(0).getLayoutParams();
        lp.width = getMeasuredWidth() + swipeDistance;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e(TAG, "onLayout:left=" + getChildAt(0).getLeft());
        Log.e(TAG, "onLayout:width=" + getChildAt(0).getWidth());
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            postInvalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }
}
