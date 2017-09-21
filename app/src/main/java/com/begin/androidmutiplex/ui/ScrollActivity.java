package com.begin.androidmutiplex.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.begin.androidmutiplex.R;
import com.begin.androidmutiplex.util.LogUtils;

public class ScrollActivity extends AppCompatActivity {

    private static final String TAG = ScrollActivity.class.getSimpleName();

    private CustomScrollView mCustomScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        mCustomScrollView = (CustomScrollView)findViewById(R.id.custom_scroll_view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mCustomScrollView.smoothScroll((int)event.getX(), (int)event.getY());
                LogUtils.log(this, "measureWidth: "+mCustomScrollView.getMeasuredWidth()+
                        ", measureHeight"+mCustomScrollView.getMeasuredHeight()+
                        ", width" + mCustomScrollView.getWidth()+
                        ", height: "+mCustomScrollView.getHeight());
                break;
        }
        return super.onTouchEvent(event);
    }
}
