package com.example.intelligentmanager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 易水柔 on 2017/3/13.
 */
public class DateUtils   {
    private static SimpleDateFormat sf = null;
    private static SimpleDateFormat sdf = null;
    private static SimpleDateFormat sddf = null;
    private static SimpleDateFormat ssddf = null;
    /*获取系统时间 格式为："yyyy/MM/dd "*/
    public static String getCurrentDate() {
        Date d = new Date();
        sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
        }
    /*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
        }
    public static String getLongToString(long time) {
        Date d = new Date(time);
        sddf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sddf.format(d);
    }
    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try{
            date = sdf.parse(time);
            } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
        return date.getTime();
        }
    public static long getStringToLong(String time) {
        ssddf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try{
            date = ssddf.parse(time);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
}
