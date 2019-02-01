package com.jhon.code.holdnet.data.dao;

import com.jhon.code.holdnet.data.Bean.SpBean;
import com.jhon.code.holdnet.data.DataConstant;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * creater : Jhon
 * time : 2019/1/24 0024
 */
@Dao
public interface SettingDao {

    @Query("SELECT * FROM " + DataConstant.VpnDb.SETTING_TABLE + " WHERE sp_key= :sp_key ")
    SpBean[] get(String sp_key);

    @Insert
    public void insert(SpBean bean);

    @Update
    public void update(SpBean bean);

    @Delete
    public void delete(SpBean bean);
}
