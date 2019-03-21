package com.jhon.code.holdnet.unit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * creater : Jhon
 * time : 2019/3/1 0001
 */
public class DateUtil {

    public final static SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm",Locale.CHINA);

    public static String format(long time){
        return mFormat.format(new Date(time));
    }




}
