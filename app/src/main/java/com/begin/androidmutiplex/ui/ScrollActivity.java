package com.begin.androidmutiplex.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.begin.androidmutiplex.R;
import com.begin.androidmutiplex.interfaces.ICDialogListener;
import com.begin.androidmutiplex.util.DialogUtils;
import com.begin.androidmutiplex.util.LogUtils;

public class ScrollActivity extends AppCompatActivity {

    private static final String TAG = ScrollActivity.class.getSimpleName();

    private CustomScrollView mCustomScrollView;

    private CommonDialog mCommonDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        mCustomScrollView = (CustomScrollView)findViewById(R.id.custom_scroll_view);


    }

    public void showCommonDialog(View view){

        DialogUtils.showDialog(this, "确定要删除该应用么?", new ICDialogListener() {
            @Override
            public void ok(Dialog dialog) {
                dialog.dismiss();
                Toast.makeText(ScrollActivity.this, "确定", Toast.LENGTH_LONG).show();
            }

            @Override
            public void cancel(Dialog dialog) {
                dialog.dismiss();
            }
        });
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
