package com.shark.apollo.deeplearning.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static String dateFormat_day = "dd";
    public static String dateFormat_month = "MM-dd";
    public static final String DATE_FORMAT_WEEK = "MM.dd";


    /**
     * 时间转换成日期，指定格式
     *
     * @param time   系统当前时间
     * @param format 日期格式
     */
    public static String timeToDate(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String weekDateToString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        StringBuilder sb = new StringBuilder(simpleDateFormat.format(date));
        if(sb.charAt(0) == '0') {
            sb.delete(0, 1);
        }
        return sb.toString();
    }

}
