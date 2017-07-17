package com.begin.androidmutiplex.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.math.BigDecimal;

/**
 * 屏幕工具类：实现获取屏幕相关参数
 *
 * @author zhouy
 */
public class DisplayUtils {

    /**
     * 获取屏幕相关参数
     *
     * @param context context
     * @return DisplayMetrics 屏幕宽高
     */
    public static DisplayMetrics getScreenSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

    /**
     * 获取屏幕density
     *
     * @param context context
     * @return density 屏幕density
     */
    public static float getDeviceDensity(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    /**
     * px转成dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        BigDecimal result = BigDecimal.valueOf(pxValue).divide(BigDecimal.valueOf(scale))
                .add(BigDecimal.valueOf(0.5));
        return result.intValue();
    }

    /**
     * dp转换成px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        BigDecimal result = BigDecimal.valueOf(dpValue).multiply(BigDecimal.valueOf(scale))
                .add(BigDecimal.valueOf(0.5));
        return result.intValue();
    }

    /**
     * 字体大小转换
     *px转换成sp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue){
        final float fontScal = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/fontScal + 0.5f);
    }

    /**
     * sp转换成px
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue){
        final float fontScal = context.getResources().getDisplayMetrics().density;
        return (int)(spValue * fontScal + 0.5f);
    }


}
