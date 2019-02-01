package com.jhon.code.holdnet.viewmodel;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.jhon.code.holdnet.VpnApplication;
import com.jhon.code.holdnet.data.Bean.AppBean;
import com.jhon.code.holdnet.unit.ExecutorManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * creater : Jhon
 * time : 2019/1/8 0008
 */
public class AppListViewModel extends ViewModel {

    LiveData<List<AppBean>> mLiveData;



    public LiveData<List<AppBean>> getAppList(){
        if(mLiveData == null) {
            mLiveData = new MutableLiveData<>();
            ExecutorManager.instance().submit(new Runnable() {
                @Override
                public void run() {
                    getApps();
                }
            });
        }
        return mLiveData;
    }


    private void getApps(){
        AppBean bean=null;
        PackageManager pm = VpnApplication.getContext().getPackageManager();
        List<PackageInfo> appInfos = pm.getInstalledPackages(0);
        List<AppBean> appBeans = new ArrayList<>();
        for(PackageInfo info:appInfos){
             bean = new AppBean();
             bean.appIcon = info.applicationInfo.loadIcon(pm);
             bean.appName = pm.getApplicationLabel(info.applicationInfo).toString();
             bean.appPackageName = info.packageName;
             bean.apkPath = info.applicationInfo.sourceDir;
            int flags = info.applicationInfo.flags;
            if((flags & ApplicationInfo.FLAG_SYSTEM) !=0){
                //那么就是系统app
                bean.isSystem = true;
            }else{
                //那么就是用户app
                bean.isSystem = false;
                appBeans.add(bean);
            }

        }
        ((MutableLiveData)mLiveData).postValue(appBeans);
    }

}
