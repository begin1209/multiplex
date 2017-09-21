package com.begin.androidmutiplex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext = null;

    private Button mShowDialog = null;

    private View mAlertView = null;

    private ListView mListView = null;

    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mShowDialog = (Button)findViewById(R.id.show_dialog);
        mAlertView = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
        mListView = (ListView)mAlertView.findViewById(R.id.alert_list_view);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.select_pic_item,
                getResources().getStringArray(R.array.select_pic_list));
        mListView.setAdapter(adapter);
        mShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        ViewConfiguration.get(this).getScaledTouchSlop();  //滑动的最小距离
    }

    private void showDialog(){
        if(null == dialog){
            dialog = new AlertDialog.Builder(this)
                    .setView(mAlertView)
                    .create();
        }
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.show_dialog_style_animation);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        if(!dialog.isShowing()){
            dialog.show();
        }else {
            dialog.cancel();
        }
//        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.horizontalMargin = 0;
        window.setAttributes(lp);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
