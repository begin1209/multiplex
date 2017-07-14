package com.begin.androidmutiplex.util;

import com.google.gson.Gson;

import java.io.Reader;

/**
 * @Author zhouy
 * @Date 2017-07-04
 */

public class GsonUtils {


    /**
     * 解析json数据
     * @param jsonData
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type){
        Gson gson = null;
        T result = null;
        try {
            gson = new Gson();
             result = gson.fromJson(jsonData, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 通过文件流读取json数据
     * @param reader
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parseJsonWithGson(Reader reader, Class<T> type){
        Gson gson = null;
        T result = null;
        try {
            gson = new Gson();
            result = gson.fromJson(reader, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
