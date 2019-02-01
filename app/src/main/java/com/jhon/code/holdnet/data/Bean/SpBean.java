package com.jhon.code.holdnet.data.Bean;

import com.jhon.code.holdnet.data.DataConstant;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * creater : Jhon
 * time : 2019/1/24 0024
 */
@Entity(tableName = DataConstant.VpnDb.SETTING_TABLE)
public class SpBean {
    @PrimaryKey@NonNull
    public String sp_key = "";

    public String value = "";

}
