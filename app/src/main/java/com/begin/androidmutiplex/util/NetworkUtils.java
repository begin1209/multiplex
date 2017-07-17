package com.begin.androidmutiplex.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * @Author zhouy
 * @Date 2017-04-27
 */

public class NetworkUtils {

    /**
     * 判断网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetworkConnect(Context context){
        if(null != context){
            ConnectivityManager manager = (ConnectivityManager)context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if(null != info){
                return info.isAvailable();
            }
        }
        return false;
    }


    /**
     * 判断wifi网络是否可用
     * @param context
     * @return
     */
    public static boolean isWifiConnect(Context context){
        if(null != context){
            ConnectivityManager manager = (ConnectivityManager)context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(null != info){
                return info.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断mobile网络是否可用
     * @param context
     * @return
     */
    public static boolean isMobileConnect(Context context){
        if(null != context){
            ConnectivityManager manager = (ConnectivityManager)context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if(null != info){
                return info.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接类型
     * @param context
     * @return
     */
    public static int getConnectType(Context context){
        if(null != context){
            ConnectivityManager manager = (ConnectivityManager)context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if(null != info && info.isAvailable()){
                return info.getType();
            }
        }
        return -1;
    }


    /**
     * 获取网络当前状态
     * @param context
     * @return
     */
    public static int getAPNType(Context context){
        int ntype = 0;
        if(null != context){
            ConnectivityManager manager = (ConnectivityManager)context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if(null == info){
                return ntype;
            }
            int nType = info.getType();
            if(ntype == ConnectivityManager.TYPE_WIFI){
                ntype = 1; //wifi
            }else if(ntype == ConnectivityManager.TYPE_MOBILE){
                int nSunType = info.getSubtype();
                TelephonyManager teleManager = (TelephonyManager)context.getSystemService(
                        Context.TELECOM_SERVICE);
                if(nSunType == TelephonyManager.NETWORK_TYPE_UMTS && !teleManager.isNetworkRoaming()){
                    ntype = 2; //2G
                }else {
                    ntype = 3; //3G
                }
            }
        }
        return ntype;
    }
}
