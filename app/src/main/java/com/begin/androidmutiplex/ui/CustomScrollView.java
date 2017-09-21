package com.begin.androidmutiplex.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * @Author zhouy
 * @Date 2017-08-07
 */

public class CustomScrollView extends TextView {


    private Scroller mScroller;

    public CustomScrollView(Context context) {
        super(context);
        init(context);
    }

    public CustomScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        mScroller = new Scroller(context);
    }

    public void smoothScroll(int desX, int desY){
        //scrolled leftPostion ,内容的滑动距离，相对view本身
        int scrollX = getScrollX();
        int dea = -desX + scrollX;
        mScroller.startScroll(scrollX, 0, dea, 0,3000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

}
