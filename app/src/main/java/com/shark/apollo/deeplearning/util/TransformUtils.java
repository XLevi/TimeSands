package com.shark.apollo.deeplearning.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransformUtils {

    private static final String TIME_ORIGIN = "00:00";
    private static final String SUFFIX_MINS = "mins";
    private static final String SUFFIX_HOURS = "hours";


    public static String time2String(int s) {
        if(s <= 0) {
            return TIME_ORIGIN;
        }
        StringBuilder timeBuilder = new StringBuilder(TIME_ORIGIN);
        int sec = s % 60;
        int min = s / 60;

        if(min < 10) {
            timeBuilder.replace(0, 1, "0");
            timeBuilder.replace(1, 2, "" + min);
        } else {
            timeBuilder.replace(0, 2, "" + min);
        }
        if(sec < 10) {
            timeBuilder.replace(3, 4, "0");
            timeBuilder.replace(4, 5, "" + sec);
        } else {
            timeBuilder.replace(3, 5, "" + sec);
        }
        return timeBuilder.toString();
    }

    public static String Minute2String(int m) {
        if(m <= 0) {
            return "00";
        }
        StringBuilder builder = new StringBuilder("00");
        if(m < 10) {
            builder.replace(0, 1, "0");
            builder.replace(1, 2, "" + m);
        } else {
            builder.replace(0, 2, "" + m);
        }
        return builder.toString();
    }

    public static String  date2String(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, HH:mm",
                Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String minutes2String(int mins) {
        if(mins < 60) {
            return String.format(Locale.getDefault(), "%d%s", mins, SUFFIX_MINS);
        } else {
            int h = mins / 60;
            int m = mins % 60;
            return String.format(Locale.getDefault(), "%d%s%d%s", h, SUFFIX_HOURS, m, SUFFIX_MINS);
        }
    }

    public static String minutesToHourString(int mins) {
        if(mins < 60) {
            return String.format(Locale.getDefault(), "%d%s", mins, SUFFIX_MINS);
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("##0.00");
            return String.format(Locale.getDefault(), "%s%s", decimalFormat.format(mins / 60.0),
                    SUFFIX_HOURS);
        }
    }

    public static String minutesToPercent(int n, int m) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0%");
        return decimalFormat.format((float) n / m);
    }

    public static String intToMonth(int m) {
        String month = "";
        switch (m) {
            case 0:
                month = "JAN";
                break;
            case 1:
                month = "FEB";
                break;
            case 2:
                month = "MAR";
                break;
            case 3:
                month = "APR";
                break;
            case 4:
                month = "MAY";
                break;
            case 5:
                month = "JUN";
                break;
            case 6:
                month = "JUL";
                break;
            case 7:
                month = "AUG";
                break;
            case 8:
                month = "SEP";
                break;
            case 9:
                month = "OCT";
                break;
            case 10:
                month = "NOV";
                break;
            case 11:
                month = "DEC";
                break;
        }
        return month;
    }

}
