package com.begin.androidmutiplex;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * @Author zhouy
 * @Date 2017-07-17
 */

public class TestApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //不选择，也可以选择直接继承MultiDexApplication
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
