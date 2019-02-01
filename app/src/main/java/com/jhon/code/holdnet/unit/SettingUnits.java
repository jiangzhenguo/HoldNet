package com.jhon.code.holdnet.unit;

import com.jhon.code.holdnet.VpnApplication;
import com.jhon.code.holdnet.data.Bean.SpBean;
import com.jhon.code.holdnet.data.dao.SettingDao;
import com.jhon.code.holdnet.data.database.VpnDataBase;

import java.util.HashMap;

/**
 * creater : Jhon
 * time : 2019/1/24 0024
 */
public class SettingUnits {

    private static HashMap<String,Object> map;
    private static SettingDao mDao = VpnDataBase.getDatabase(VpnApplication.getContext()).getSettingDao();


    public static void setValue(String key,Object object){
        if(canSave(object)){
            setValue2(key,object);
        }
    }


    public static boolean getBooleanValue(String key){
        if(map.containsKey(key)){
            Object o = map.get(key);
            if(o instanceof Boolean) {
                return (Boolean)map.get(key);
            }
        } else {
            SpBean[] beans = mDao.get(key);
            if(beans != null && beans.length > 0){
                SpBean bean = beans[0];
                if(bean != null){
                    Boolean o = Boolean.valueOf(bean.value);
                     map.put(bean.sp_key,o);
                     return o;
                }
            }
        }
        return false;
    }


    public static String getStringValue(String key){
        if(map.containsKey(key)){
            Object o = map.get(key);
            if(o instanceof Boolean) {
                return (String)map.get(key);
            }
        } else {
            SpBean[] beans = mDao.get(key);
            if(beans != null && beans.length > 0){
                SpBean bean = beans[0];
                if(bean != null){
                    String o = bean.value;
                    map.put(bean.sp_key,o);
                    return o;
                }
            }
        }
        return "";
    }

    public static int getIntValue(String key){
        if(map.containsKey(key)){
            Object o = map.get(key);
            if(o instanceof Boolean) {
                return (int)map.get(key);
            }
        } else {
            SpBean[] beans = mDao.get(key);
            if(beans != null && beans.length > 0){
                SpBean bean = beans[0];
                if(bean != null){
                    int o = Integer.parseInt(bean.value);
                    map.put(bean.sp_key,o);
                    return o;
                }
            }
        }
        return 0;
    }

    public static double getDoubleValue(String key){
        if(map.containsKey(key)){
            Object o = map.get(key);
            if(o instanceof Boolean) {
                return (int)map.get(key);
            }
        } else {
            SpBean[] beans = mDao.get(key);
            if(beans != null && beans.length > 0){
                SpBean bean = beans[0];
                if(bean != null){
                    double o = Double.parseDouble(bean.value);
                    map.put(bean.sp_key,o);
                    return o;
                }
            }
        }
        return 0;
    }


    private static boolean canSave(Object object){

        if(object instanceof Integer || object instanceof Double || object instanceof String ||
                object instanceof Boolean){
            return true;
        } else {
            return false;
        }

    }


    private static void setValue2(String key,Object object){
        SpBean bean =new SpBean();
        if(map.containsKey(key)){
            bean.sp_key = key;
            bean.value = String.valueOf(object);
            mDao.update(bean);
        } else {
            bean.sp_key =key;
            bean.value = String.valueOf(object);
            mDao.insert(bean);
        }
        map.put(key,object);
    }












}
