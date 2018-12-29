package com.jhon.code.holdnet.data.Bean;

import com.jhon.code.holdnet.data.DataConstant;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * creater : Jhon
 * time : 2018/12/28 0028
 */
@Entity(tableName = DataConstant.VpnDb.PROJECT_TABLE)
public class VpnProject {

    @PrimaryKey
    public int _id;

    public String projectName;

    public String packageName;
}
