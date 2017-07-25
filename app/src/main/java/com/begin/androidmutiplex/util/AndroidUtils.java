package com.begin.androidmutiplex.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @Author zhouy
 * @Date 2017-05-05
 */

public class AndroidUtils {

    /**
     * 获取包信息
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException{
        PackageManager manager = context.getPackageManager();
        PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        return info == null ? null: info;
    }

    /**
     *获取设备IMEI
     * @param context
     * @return
     */
    public static String getDeviceIMEI(Context context){
        TelephonyManager manager = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        String imei = manager.getDeviceId();
        return imei == null? "": imei;

    }

    /**
     *获取AndroidID
     * @param context
     * @return
     */
    public static String getDeviceAndroidID(Context context){
        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return null== androidId? "": androidId;
    }


    /**
     * 获取IP和MAC地址
     * @param context
     * @return
     */
    public static String[] getIPAndMac(Context context){
        String ip = null;
        String mac = null;
        WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        if(null != info && manager.getWifiState() == WifiManager.WIFI_STATE_ENABLED){
            int ipAddress = info.getIpAddress();

            ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
            mac = info.getMacAddress();
            if(null != mac){
                mac.replaceAll(":","");
            }
        }
        if (TextUtils.isEmpty(mac) || TextUtils.isEmpty(ip)){
            boolean flag = false;
            NetworkInterface netInterface;
            InetAddress inetAddress;
            try{
                for (Enumeration<NetworkInterface> enumNetInterface = NetworkInterface.getNetworkInterfaces(); enumNetInterface.hasMoreElements(); ) {
                    netInterface = enumNetInterface.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = netInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            ip = inetAddress.getHostAddress();
                            mac = ConvertUtils.bytes2HexStr(netInterface.getHardwareAddress());
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        break;
                    }
                }
            }catch (Exception e){

            }
        }

        String[] results = new String[2];
        results[0] = ip;
        results[1] = mac;
        return results;
    }

    /**
     * 拨打电话
     * @param context
     * @param phoneNumber
     */
    public static void callPhone(Context context, String phoneNumber){
        try {
            if (context != null) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }catch(Exception ex){
        }
    }

    /**
     * 发送邮件
     * @param context
     * @param target
     * @param body
     * @param subject
     * @param cc
     */
    public static void sendEmail(Context context, String[] target, String body, String subject, String[] cc){
        try {
            if (context != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, target);
                intent.putExtra(Intent.EXTRA_CC, cc);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                context.startActivity(intent);
            }
        }catch(Exception ex){
        }
    }

    /**
     * 调用系统界面发送短信
     * @param context
     * @param target
     * @param body
     */
    public static void sendSMS(Context context, String target, String body){
        try {
            if (context != null) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + target));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("sms_body", body);
                context.startActivity(intent);
            }
        }catch (Exception ex){

        }
    }

    /**
     * 直接发送短信
     * @param context
     * @param target
     * @param body
     * @param sentIntent
     * @param deliveryIntent
     */
    public static void sendSMSDirect(Context context, String target, String body, Intent sentIntent, Intent deliveryIntent){
        try {
            if (context != null) {
                PendingIntent sentPending = sentIntent == null ? null : PendingIntent.getBroadcast(context, 0, sentIntent, 0);
                PendingIntent deliveryPending = deliveryIntent == null ? null : PendingIntent.getBroadcast(context, 0, deliveryIntent, 0);
                SmsManager manager = SmsManager.getDefault();
                manager.sendTextMessage(target, null, body, sentPending, deliveryPending);

            }
        }catch(Exception ex){

        }
    }

    /**
     * 系统打开网页
     * @param context
     * @param url
     */
    public static void openWeb(Context context, String url){
        try {
            if (context != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }catch(Exception ex){
        }
    }


    /**
     * 从磁盘获取文件路径
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName){
        try {
            String cachePath;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                cachePath = context.getExternalCacheDir().getPath();
            } else {
                cachePath = context.getCacheDir().getPath();
            }
            File result = new File(cachePath + File.separator + uniqueName);
            if (!result.exists()) {
//                LogUtil.log("disk cache directory create");
                result.mkdirs();
            }
            return result;
        }catch(Exception ex){
//            LogUtil.log("读取磁盘缓存路径失败：", ex);
        }
        return null;
    }



}
