package com.funstill.kelefun.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author liukaiyang
 * @since 2017/3/27 15:26
 */

public class DateUtil {
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_MONTH = 2592000000L;

    private static final String ONE_SECOND_AGO = "秒前 ";
    private static final String ONE_MINUTE_AGO = "分钟前 ";
    private static final String ONE_HOUR_AGO = "小时前 ";
    private static final String ONE_DAY_AGO = "天前 ";
    private static final String ONE_MONTH_AGO = "月前 ";
    private static final String YESTERDAY = "昨天 ";
    private static final String[] numChinese= {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖","拾"};
    private static final String TIME_PATTERN_FANFOU = "EEE MMM dd HH:mm:ss Z yyyy";
    public static final String TIME_PATTERN_LINE = "yyyy-MM-dd HH:mm:ss";

    private static final SimpleDateFormat FANFOU_DATE_FORMAT = new SimpleDateFormat(TIME_PATTERN_FANFOU, Locale.US);
    private static final ParsePosition mPosition = new ParsePosition(0);

    private static Date fanfouToDate(String s) {
        // Fanfou Date String example --> "Mon Dec 13 03:10:21 +0000 2010"
        // final ParsePosition position = new ParsePosition(0);//
        mPosition.setIndex(0);
        return FANFOU_DATE_FORMAT.parse(s, mPosition);
    }

    public static String dateToString(Date dt, String sFmt) {
        String sRet;
        SimpleDateFormat sdfFrom = null;
        sRet = null;
        try {
            sdfFrom = new SimpleDateFormat(sFmt);
            sRet = sdfFrom.format(dt).toString();
        } catch (Exception ex) {
            LogHelper.e(ex.getMessage());
            return null;
        }
        return sRet;
    }

    /**
     * 转化字符型的时间戳为几分钟前,几小时前等的形式
     * @author liukaiyang 2016年11月1日
     * @param dateStr
     * @return
     */
    public static String toAgo(String dateStr){
        return toAgo(fanfouToDate(dateStr));
    }

    /**
     * 转换字符型的时间戳为 大写的年<br>
     * 比如:当前时间为 2017-06-11 18:35:35----参数为2016-11-11 18:35:35 执行函数后则显示为 零.柒<br>
     *    TODO 目前只支持100年内
     * @param dateStr
     * @return
     */
    public static String toYear(String dateStr){
        Date date= fanfouToDate(dateStr);
        long delta = (Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")).getTimeInMillis() - date.getTime());
        long month=toMonths(delta);
        int y=(int) month/12;
        int m= (int) month%12;
        String yearStr,monthStr;
        if(y>10){
            int ten=y/10;//十位数
            yearStr= numChinese[ten]+numChinese[y%10];
        }else{
            yearStr=numChinese[y];//只有个位数
        }
        if(m>10){
            monthStr= numChinese[10]+numChinese[m%10];
        }else{
            monthStr=numChinese[m];
        }
        return  yearStr+"."+monthStr;
    }

    /**
     * 将时间戳转换为多少时间之前<br>
     * 比如:当前时间为 2016-11-11 18:35:35----参数为2016-11-11 18:32:35--执行函数后则显示为3分钟前<br>
     * 如果为一年之前则直接显示时间
     * @author liukaiyang 2016年11月1日
     * @param date
     * @return
     */
    public static String toAgo(Date date) {

        long delta = (Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")).getTimeInMillis() - date.getTime());

        if (delta < ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < ONE_HOUR) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < ONE_DAY) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 2L * ONE_DAY) {
            return YESTERDAY;
        }
        //1个月内
        if (delta < ONE_MONTH) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        //6个月内
        if (delta < 6L * ONE_MONTH) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            //更早则直接显示时间
            return dateToString(date,TIME_PATTERN_LINE);
        }
    }


    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

}
