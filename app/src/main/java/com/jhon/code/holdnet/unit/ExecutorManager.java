package com.jhon.code.holdnet.unit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * creater : Jhon
 * time : 2019/1/8 0008
 */
public class ExecutorManager {

    private ExecutorService mCacheThreadPool;

    private static ExecutorManager mManager;

    public static ExecutorManager instance(){
        if(mManager == null) {
            synchronized (ExecutorManager.class) {
               if(mManager == null){
                   mManager = new ExecutorManager();
               }
            }
        }
        return mManager;
    }

    private ExecutorManager(){
        mCacheThreadPool = Executors.newCachedThreadPool();
    }



    public void submit(Runnable runnable){
        mCacheThreadPool.submit(runnable);
    }

    public void dispose(){
        if(!mCacheThreadPool.isShutdown())
        mCacheThreadPool.shutdown();
    }




}
