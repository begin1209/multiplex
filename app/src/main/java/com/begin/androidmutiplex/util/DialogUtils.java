package com.begin.androidmutiplex.util;

import android.content.Context;

import com.begin.androidmutiplex.interfaces.ICDialogListener;
import com.begin.androidmutiplex.ui.CommonDialog;

/**
 * @Author zhouy
 * @Date 2017-09-22
 */

public class DialogUtils {

    private static CommonDialog mCommonDialog;

    public static void showDialog(Context context, String message, ICDialogListener listener){
        showDialog(context,"提示", message, "确定", "取消", listener);
    }

    public static void showDialog(Context context, String title, String message, ICDialogListener listener){
        showDialog(context, title, message, "确定", "取消", listener);
    }


    public static void showDialog(Context context, String title, String message,
                                  String posStr, ICDialogListener listener){
        showDialog(context, title,message, posStr, "取消", listener);
    }

    public static void showDialog(Context context, String title, String message,
                                  String posStr, String cancelStr, ICDialogListener listener){
        if(null == mCommonDialog){
            mCommonDialog = new CommonDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveText(posStr)
                    .setNegativeText(cancelStr)
                    .build();
            mCommonDialog.setICDialogListener(listener);
            mCommonDialog.show();
        }else {
            if(!mCommonDialog.isShowing()){
                mCommonDialog.show();
            }
        }

    }
}
