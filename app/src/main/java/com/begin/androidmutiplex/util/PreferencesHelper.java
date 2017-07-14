package com.begin.androidmutiplex.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Author zhouy
 * @Date 2017-04-11
 */

public class PreferencesHelper {

    private static SharedPreferences sharedPreferences;

    public PreferencesHelper(Context context, String name){
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public boolean saveStringValue(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getStringValue(String key){
        return sharedPreferences.getString(key, "");
    }

    public boolean saveIntValue(String key, int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public int getIntValue(String key){
        return sharedPreferences.getInt(key, -1);
    }

    public boolean saveBooleanValue(String key, boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean getBooleanValue(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean checkKeyInvalid(String key){
        return sharedPreferences.contains(key);
    }

    public void clearPreference(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
