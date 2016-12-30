package com.practice.HScrollItemViewPractice;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by gaofeng on 2016-12-30.
 */

public class MyLinearLayout extends LinearLayout {
    private static final String TAG = MyLinearLayout.class.getSimpleName();

    TextView coverTv;
    TextView itemATv;
    TextView itemBTv;
    int horizontalScrollDistance = 0;
    int lastX;
    Scroller scroller;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        coverTv = (TextView) findViewById(R.id.coverTv);
        itemATv = (TextView) findViewById(R.id.itemATv);
        itemBTv = (TextView) findViewById(R.id.itemBTv);
        //itemATv.setPivotX(0.5f);
        //itemATv.setPivotY(0.5f);
        //itemBTv.setPivotX(0.5f);
        //itemBTv.setPivotY(0.5f);
        scroller = new Scroller(getContext());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) ev.getX();
                scroller.abortAnimation();
                return false;
            case MotionEvent.ACTION_MOVE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        x = Math.max(0, Math.min(x, horizontalScrollDistance));
        float scrollFactor=(float)x/horizontalScrollDistance;
        itemATv.setScaleY(Math.max(0f, Math.min(scrollFactor*2, 1f)));
        itemBTv.setScaleY(Math.max(0f, Math.min(scrollFactor, 1f)));
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),0);
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (lastX - event.getX());
                lastX = (int) event.getX();
                scrollBy(dx, 0);
                break;

            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouchEvent: ACTION_UP" );
                if (getScrollX() > horizontalScrollDistance / 2) {
                    scroller.startScroll(getScrollX(), 0, horizontalScrollDistance - getScrollX(), 0, 300);
                } else {
                    scroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 300);
                }
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        horizontalScrollDistance = itemATv.getMeasuredWidth() + itemBTv.getMeasuredWidth();
        Log.e(TAG, "onMeasure: itemATv.width=" + itemATv.getMeasuredWidth() + " itemBTv.width=" + itemBTv.getMeasuredWidth());
    }
}