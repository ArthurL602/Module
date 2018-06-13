package com.ljb.baselibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间日期工具类
 */
public class DateUtil {

    private static DateUtil dataUtil;
    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     **/
    private static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式：yyyy-MM-dd HH:mm
     **/
    private static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    /**
     * 日期格式：yyyy-MM-dd
     **/
    private static final String DF_YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 日期格式：MM-dd HH:mm
     **/
    private static final String DF_MM_DD_HH_MM = "MM-dd HH:mm";
    /**
     * 日期格式：MM-dd
     **/
    private static final String DF_MM_DD = "MM-dd";


    /**
     * 日期格式：HH:mm:ss
     **/
    private static final String DF_HH_MM_SS = "HH:mm:ss";
    /**
     * 日期格式：HH:mm
     **/
    private static final String DF_HH_MM = "HH:mm";

    /**
     * 日期格式：yyyy年MM月dd日 HH时mm分ss秒
     **/
    private static String DF_C_YYYY_MM_DD_HH_MM_SS = "yyyy年MM月dd日 HH时mm分ss秒";
    /**
     * 日期格式：yyyy年MM月dd日 HH时mm分
     **/
    private static String DF_C_YYYY_MM_DD_HH_MM = "yyyy年MM月dd日 HH时mm分";
    /**
     * 日期格式：yyyy年MM月dd日 HH时
     **/
    private static String DF_C_YYYY_MM_DD_HH = "yyyy年MM月dd日 HH时";
    /**
     * 日期格式：yyyy年MM月dd日
     **/
    private static String DF_C_YYYY_MM_DD = "yyyy年MM月dd日";
    /**
     * 日期格式：MM月dd日HH时mm分
     **/
    private static final String DF_C_MM_DD_HH_MM = "MM月dd日HH时mm分";
    /**
     * 日期格式：MM月dd日
     **/
    private static final String DF_C_MM_DD = "MM月dd日";
    /**
     * 日期格式：时 分秒
     **/
    private static final String DF_C_HH_MM_SS = "HH时mm分ss秒";
    /**
     * 日期格式：时 分
     **/
    private static final String DF_C_HH_MM = "HH时mm 分";

    private static final String DATE_FORMAT = "%4d-%02d-%02d";

    /**
     * 日期格式枚举
     */
    public enum DateType {
        DF_YYYY_MM_DD_HH_MM_SS, DF_YYYY_MM_DD_HH_MM, DF_YYYY_MM_DD, DF_MM_DD_HH_MM, DF_MM_DD, DF_HH_MM_SS, DF_HH_MM,
        DF_C_YYYY_MM_DD_HH_MM_SS, DF_C_YYYY_MM_DD_HH_MM, DF_C_YYYY_MM_DD_HH, DF_C_YYYY_MM_DD, DF_C_MM_DD_HH_MM,
        DF_C_MM_DD, DF_C_HH_MM_SS, DF_C_HH_MM;
    }

    /**
     * 初始、实例化
     */
    public static DateUtil getInstance() {
        if (dataUtil == null) {
            dataUtil = new DateUtil();
        }
        return dataUtil;
    }

    /**
     * 时间（日期）转字符串
     */
    public String dateToString(Date date, DateType type) {
        SimpleDateFormat format = null;
        format = setDateFormat(format, type);
        //转为字符串
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转为时间
     */
    public Date stringToDate(String strDate, DateType type) {
        SimpleDateFormat format = null;
        format = setDateFormat(format, type);
        try {
            return format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据日期类型实例化一个SimpleDateFormat
     *
     * @param format
     * @param type
     */
    private SimpleDateFormat setDateFormat(SimpleDateFormat format, DateType type) {
        switch (type) {
            case DF_YYYY_MM_DD_HH_MM_SS:
                format = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
                break;
            case DF_YYYY_MM_DD_HH_MM:
                format = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM);
                break;
            case DF_YYYY_MM_DD:
                format = new SimpleDateFormat(DF_YYYY_MM_DD);
                break;
            case DF_MM_DD_HH_MM:
                format = new SimpleDateFormat(DF_MM_DD_HH_MM);
                break;
            case DF_MM_DD:
                format = new SimpleDateFormat(DF_MM_DD);
                break;
            case DF_HH_MM_SS:
                format = new SimpleDateFormat(DF_HH_MM_SS);
                break;
            case DF_HH_MM:
                format = new SimpleDateFormat(DF_HH_MM);
                break;
            case DF_C_YYYY_MM_DD_HH_MM_SS:
                format = new SimpleDateFormat(DF_C_YYYY_MM_DD_HH_MM_SS);
                break;
            case DF_C_YYYY_MM_DD_HH_MM:
                format = new SimpleDateFormat(DF_C_YYYY_MM_DD_HH_MM);
                break;
            case DF_C_YYYY_MM_DD_HH:
                format = new SimpleDateFormat(DF_C_YYYY_MM_DD_HH);
                break;
            case DF_C_YYYY_MM_DD:
                format = new SimpleDateFormat(DF_C_YYYY_MM_DD);
                break;
            case DF_C_MM_DD_HH_MM:
                format = new SimpleDateFormat(DF_C_MM_DD_HH_MM);
                break;
            case DF_C_MM_DD:
                format = new SimpleDateFormat(DF_C_MM_DD);
                break;
            case DF_C_HH_MM_SS:
                format = new SimpleDateFormat(DF_C_HH_MM_SS);
                break;
            case DF_C_HH_MM:
                format = new SimpleDateFormat(DF_C_HH_MM);
                break;
        }
        return format;
    }

    /**
     * 根据日期获取星期
     *
     * @param date
     * @return
     */
    public String getWeekByDate(Date date, int type) {
        String week = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (day) {
            case 0:
                week = "日";
                break;
            case 1:
                week = "一";
                break;
            case 2:
                week = "二";
                break;
            case 3:
                week = "三";
                break;
            case 4:
                week = "四";
                break;
            case 5:
                week = "五";
                break;
            case 6:
                week = "六";
                break;
        }
        if (type == 0) {
            week = "周" + week;
        } else {
            week = "星期" + week;
        }
        return week;
    }

    /**
     * 距离今天多久
     *
     * @param date
     * @return
     */
    public static String fromToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        long time = date.getTime() / 1000;
        long now = new Date().getTime() / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR) return ago / ONE_MINUTE + "分钟前";
        else if (ago <= ONE_DAY) return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟前";
        else if (ago <= ONE_DAY * 2)
            return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
        else if (ago <= ONE_DAY * 3)
            return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
        else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            return day + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            return month + "个月" + day + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar
                    .MINUTE) + "分";
        } else {
            long year = ago / ONE_YEAR;
            int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0 so month+1
            return year + "年前" + month + "月" + calendar.get(Calendar.DATE) + "日";
        }
    }

    /**
     * 距离截止日期还有多长时间
     *
     * @param date
     * @return
     */
    public static String fromDeadline(Date date) {
        long deadline = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long remain = deadline - now;
        if (remain <= ONE_HOUR) return "只剩下" + remain / ONE_MINUTE + "分钟";
        else if (remain <= ONE_DAY) return "只剩下" + remain / ONE_HOUR + "小时" + (remain % ONE_HOUR / ONE_MINUTE) + "分钟";
        else {
            long day = remain / ONE_DAY;
            long hour = remain % ONE_DAY / ONE_HOUR;
            long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
        }
    }

    /**
     * 距离今天的绝对时间
     *
     * @param date
     * @return
     */
    public static String toToday(Date date) {
        long time = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR) return ago / ONE_MINUTE + "分钟";
        else if (ago <= ONE_DAY) return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟";
        else if (ago <= ONE_DAY * 2)
            return "昨天" + (ago - ONE_DAY) / ONE_HOUR + "点" + (ago - ONE_DAY) % ONE_HOUR / ONE_MINUTE + "分";
        else if (ago <= ONE_DAY * 3) {
            long hour = ago - ONE_DAY * 2;
            return "前天" + hour / ONE_HOUR + "点" + hour % ONE_HOUR / ONE_MINUTE + "分";
        } else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            long hour = ago % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return day + "天前" + hour + "点" + minute + "分";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            long hour = ago % ONE_MONTH % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_MONTH % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return month + "个月" + day + "天" + hour + "点" + minute + "分前";
        } else {
            long year = ago / ONE_YEAR;
            long month = ago % ONE_YEAR / ONE_MONTH;
            long day = ago % ONE_YEAR % ONE_MONTH / ONE_DAY;
            return year + "年前" + month + "月" + day + "天";
        }

    }

    /**
     * 将String的日期转换成 毫秒
     * @param date
     * @param dateType
     * @return
     */
    public long StrDateToLong(String date, DateType dateType) {
        SimpleDateFormat format = setDateFormat(new SimpleDateFormat(), dateType);
        String time = format.format(date);
        Date d = null;
        try {
            d = format.parse(time);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取指定时间的前（后）几天时间
     *
     * @param date
     * @param day
     * @return
     */
    public Date getDayBefore(Date date, int day) {
        long mTime = date.getTime();
        long beforeTime = mTime - day * 24 * 3600 * 1000;
        return new Date(beforeTime);
    }

    /**
     * 获取指定时间的前（后）几小时时间
     *
     * @param date
     * @param Hour
     * @return
     */
    public Date getHourBefore(Date date, int Hour) {
        long mTime = date.getTime();
        long beforeTime = mTime - Hour * 3600 * 1000;
        return new Date(beforeTime);
    }

    /**
     * 判断两个日期是否是同一天
     *
     * @param date
     * @param date1
     * @return
     */
    public boolean isSameDay(Date date, Date date1) {
        return dateToString(date, DateType.DF_YYYY_MM_DD).equals(dateToString(date1, DateType.DF_YYYY_MM_DD));
    }

    /**
     * 与当前时间的时间距离
     *
     * @param startDate 开始时间
     * @return 返回距离发帖时间的时间差
     */
    public String twoDateDistance(Date startDate) {
        if (startDate == null) {
            return null;
        }
        long timeLong = new Date().getTime() - startDate.getTime();
        if (timeLong <= 0) {
            return "刚刚";
        } else if (timeLong < 60 * 1000) {
            return timeLong / 1000 + "秒前";
        } else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        } else {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        }
    }
}
