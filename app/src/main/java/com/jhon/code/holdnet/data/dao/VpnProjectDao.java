package com.jhon.code.holdnet.data.dao;

import com.jhon.code.holdnet.data.Bean.VpnProject;
import com.jhon.code.holdnet.data.DataConstant;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * creater : Jhon
 * time : 2018/12/28 0028
 */
@Dao
public interface VpnProjectDao {

    @Query("SELECT * FROM " + DataConstant.VpnDb.PROJECT_TABLE)
   LiveData<List<VpnProject>> getAll();
    @Delete
    void deleteProject(VpnProject... projects);
    @Update
    void updateProject(VpnProject... projects);
    @Insert
    void insertProject(VpnProject... projects);
}
