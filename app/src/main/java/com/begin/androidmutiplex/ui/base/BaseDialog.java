package com.begin.androidmutiplex.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.begin.androidmutiplex.util.DisplayUtils;

/**
 * @Author zhouy
 * @Date 2017-09-22
 */

public abstract class BaseDialog extends Dialog {

    private View view;

    public BaseDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(@NonNull Context context){
        //设置Dialog的大小和位置
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = (int)(DisplayUtils.getScreenSize(context).widthPixels * 0.8);
        window.setAttributes(layoutParams);
    }

}
