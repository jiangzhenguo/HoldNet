package com.jhon.code.holdnet.data.repository;

import android.content.Context;

import com.jhon.code.holdnet.data.Bean.VpnProject;
import com.jhon.code.holdnet.data.dao.VpnProjectDao;
import com.jhon.code.holdnet.data.database.VpnDataBase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

/**
 * creater : Jhon
 * time : 2018/12/29 0029
 */
public class VpnRepository {
    private VpnProjectDao mProjectDao;
    private ExecutorService mPool;
    private static VpnRepository mRepository;
    public static VpnRepository instance(Context context){
        synchronized (VpnRepository.class){
            if(mRepository == null){
                mRepository = new VpnRepository(context);
            }
        }
        return mRepository;
    }

    private VpnRepository(Context context){
        VpnDataBase dataBase = VpnDataBase.getDatabase(context);
        mProjectDao = dataBase.getVpnProjectDao();
        mPool = Executors.newCachedThreadPool();
    }


    public LiveData<List<VpnProject>> getProject(){
        if(mProjectDao != null){
            return mProjectDao.getAll();
        }
        return null;
    }

    public void insertProject(final VpnProject project){
        mPool.submit(new Runnable() {
            @Override
            public void run() {
                if(mProjectDao != null){
                    mProjectDao.insertProject(project);
                }
            }
        });
    }

    public void updateProject(final VpnProject project){
        mPool.submit(new Runnable() {
            @Override
            public void run() {
                if(mProjectDao != null){
                    mProjectDao.updateProject(project);
                }
            }
        });
    }

    public void deleteProject(final VpnProject project){
        mPool.submit(new Runnable() {
            @Override
            public void run() {
                if(mProjectDao != null){
                    mProjectDao.deleteProject(project);
                }
            }
        });
    }


}
