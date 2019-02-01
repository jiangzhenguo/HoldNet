package com.jhon.code.holdnet.data.database;

import android.content.Context;

import com.jhon.code.holdnet.data.Bean.SpBean;
import com.jhon.code.holdnet.data.Bean.VpnProject;
import com.jhon.code.holdnet.data.DataConstant;
import com.jhon.code.holdnet.data.dao.SettingDao;
import com.jhon.code.holdnet.data.dao.VpnProjectDao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * creater : Jhon
 * time : 2018/12/29 0029
 */
@Database(entities = {VpnProject.class,SpBean.class},version = 1)
public abstract class VpnDataBase extends RoomDatabase {


    private static volatile VpnDataBase instance;

    public static VpnDataBase getDatabase(final Context context){
        if(instance == null){
            synchronized (VpnDataBase.class){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),VpnDataBase.class,DataConstant.VpnDb.NAME).build();
                }
            }
        }
        return instance;
    }

    public abstract VpnProjectDao getVpnProjectDao();


    public abstract SettingDao getSettingDao();
}
