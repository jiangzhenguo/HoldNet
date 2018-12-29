package com.jhon.code.holdnet;

import android.app.Application;
import android.content.Context;

/**
 * creater : Jhon
 * time : 2018/12/29 0029
 */
public class VpnApplication extends Application {

    private static VpnApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext(){
         return instance;
     }
}
