package com.begin.androidmutiplex.controller;

import android.content.Context;

import com.begin.androidmutiplex.util.LogUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建线程池，可配
 * @Author zhouy
 * @Date 2017-04-11
 */

public class TaskManager {

    private static Context sContext;

    private static volatile TaskManager manager;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    private static final int KEEP_ALIVE_SECONDS = 30;


    private static final BlockingQueue<Runnable> sPoolWorkQuene = new LinkedBlockingQueue<>(128);

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "TaskManager #" + mCount.getAndIncrement());
        }
    };

    private static final Executor THREAD_POOL_EXECUTOR;

    static {
        ThreadPoolExecutor sThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS, sPoolWorkQuene,sThreadFactory);
        THREAD_POOL_EXECUTOR = sThreadPoolExecutor;
    }

    private TaskManager(Context context){
        if(null != context){
            sContext = context.getApplicationContext();
        }
    }


    public static TaskManager getInstance(Context context){
        if(null == manager){
            synchronized (TaskManager.class){
                if(null == manager){
                    manager = new TaskManager(context);
                }
            }
        }
        return manager;
    }


    public void  execute (Runnable runnable){
        THREAD_POOL_EXECUTOR.execute(runnable);
    }
}
