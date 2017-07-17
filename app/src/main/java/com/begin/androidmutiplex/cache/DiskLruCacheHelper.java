package com.begin.androidmutiplex.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 主要表示DiskLruCache的使用方法
 * @Author zhouy
 * @Date 2017-07-17
 */

public class DiskLruCacheHelper {

    private static final long MAX_CACHE_SIZE = 1024 * 1024 * 50;  //50M

    private DiskLruCache mDiskLruCache;

    /**
     * 缓存创建
     * @param directory
     * @param valueCount
     * @param maxSize
     */
    public DiskLruCacheHelper(File directory, int valueCount, long maxSize){
        try {
            //appVersion开发中不经常用，valueCount设置为1，这在newOutputStream中index为0
            mDiskLruCache = DiskLruCache.open(directory, 1, 1, MAX_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 缓存添加
     * @param key
     * @param inputStream
     */
    public void put(String key, InputStream inputStream){
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            //通过DiskLruCache.Editor添加
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if(null != editor){
                //BufferInputStream和BufferOutputStream
                bis = new BufferedInputStream(inputStream);
                //由于前面创建时，允许一个节点写入一个数据，所以index设置为0
                bos = new BufferedOutputStream(editor.newOutputStream(0));
                int length = 0;
                while ((length = bis.read()) != -1){
                    bos.write(length);
                }
                //写入成功使用editor.commit, 写入失败用editor.abort,上述步骤还未真正将文件流写入到文件中
                editor.commit();
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(null != bos){
                    bos.close();
                }
                if(null != bis){
                    bis.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 缓存获取是通过DiskLruCache.Snapshot获取的
     * @param key
     * @return
     */
    public InputStream get(String key){
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if(null != snapshot){
                FileInputStream inputStream = (FileInputStream)snapshot.getInputStream(0);
                return inputStream;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
