package com.begin.androidmutiplex.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

import com.begin.androidmutiplex.controller.TaskManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 崩溃日志的搜集,本地存储和上传至服务器,注意存储文件时设备的存储权限问题
 * @Author zhouy
 * @Date 2017-04-28
 */

public class CrashHandler implements UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    private static final boolean DEBUG = true;

    private static String PATH = null;

    private static final String FILE_NAME = "crash";

    private static final String FILE_NAME_SUFFIX = ".trace";

    // 用来存储设备信息和异常信息
    private Map<String, String> crashInfo = new HashMap<String, String>();

    private static CrashHandler sInstance = new CrashHandler();

    private UncaughtExceptionHandler mDefaultExceptionHandler;

    private Context mContext;


    private CrashHandler(){

    }

    public static CrashHandler getInstance(){
        return sInstance;
    }

    public void init(Context context){
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        if(null != context){
            mContext = context.getApplicationContext();
            PATH = context.getExternalFilesDir(null).getAbsolutePath()+"/crash/";
        }
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {

        if(!handleException(e) && mDefaultExceptionHandler != null){
            mDefaultExceptionHandler.uncaughtException(t, e);
        }else {
            try {
                Thread.sleep(3000);
            }catch (InterruptedException interruptEx){
                interruptEx.printStackTrace();
            }
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex){
        if(null == ex){
            return false;
        }
        saveCrashInfoInFile(ex);
        sendCrashToServer(ex);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉，程序遇到异常，即将退出", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        };

        TaskManager.getInstance(mContext).execute(runnable);
        return true;
    }

    /**
     * 保存crash日志到文件中
     * @param throwable
     */
    private void saveCrashInfoInFile(Throwable throwable){

        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            if(DEBUG){
                LogUtils.w(TAG, "sdcard unmounted, skip save exception");
                return;
            }
        }
        try{
            File file = new File(PATH);
            if(!file.exists()){
                file.mkdirs();
            }
            long current = System.currentTimeMillis();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
            File crashName = new File(PATH+FILE_NAME+time+FILE_NAME_SUFFIX);
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(crashName)));
            pw.println(time);
            savePhoneInfo(pw);
            pw.println();
            throwable.printStackTrace(pw);
            pw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 保存手机信息
     * @param printWriter
     * @throws PackageManager.NameNotFoundException
     */
    private void savePhoneInfo(PrintWriter printWriter) throws PackageManager.NameNotFoundException{
        PackageManager pm = mContext.getPackageManager();
        PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        if(null != info){
            //App版本与版本号
            printWriter.print("App Version: ");
            printWriter.print(info.versionName);
            printWriter.print("_");
            printWriter.println(info.versionCode);
        }

        //android版本号
        printWriter.print("OS version: ");
        printWriter.print(Build.VERSION.RELEASE);
        printWriter.print("_");
        printWriter.println(Build.VERSION.SDK_INT);

        //手机制造商
        printWriter.print("Vendor: ");
        printWriter.println(Build.MANUFACTURER);
        //手机型号
        printWriter.print("Model: ");
        printWriter.println(Build.MODEL);
        //CPU架构
        printWriter.print("CPU ABI: ");
        printWriter.println(Build.CPU_ABI);
    }


    /**
     * 发送crash日志到服务器
     * @param ex
     */
    private void sendCrashToServer(Throwable ex){

        try {
            //取出版本号
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                crashInfo.put("versionName", versionName);
                crashInfo.put("versionCode", versionCode);
            }
            ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            String pageName = mContext.getClass().getName();
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);
            String memoryInfo = "Memory info:" + mi.availMem + ",app holds:"
                    + mi.threshold + ",Low Memory:" + mi.lowMemory;

            ApplicationInfo appInfo = mContext.getPackageManager()
                    .getApplicationInfo(mContext.getPackageName(),
                            PackageManager.GET_META_DATA);
            //获取channelID
//            String channelId = appInfo.metaData.getString("UMENG_CHANNEL");

            String version = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionName;

            HashMap<String, String> exceptionInfo = new HashMap<String, String>();

            exceptionInfo.put("PageName", pageName);
            exceptionInfo.put("ExceptionName", ex.getClass().getName());
            exceptionInfo.put("ExceptionType", "1");
            exceptionInfo.put("ExceptionsStackDetail", "################");
            exceptionInfo.put("AppVersion", version);
            exceptionInfo.put("OSVersion",  Build.VERSION.RELEASE);
            exceptionInfo.put("DeviceModel", Build.MODEL);
            exceptionInfo.put("DeviceId", "");
            exceptionInfo.put("NetWorkType", String.valueOf(NetworkUtils.isWifiConnect(mContext)));
            exceptionInfo.put("ChannelId", "##################");
            exceptionInfo.put("ClientType", "100");
            exceptionInfo.put("MemoryInfo", memoryInfo);

            final String rquestParam = exceptionInfo.toString();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //执行上传网络请求
                }
            };
            TaskManager.getInstance(mContext).execute(runnable);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
    }

}
