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

	/** 日期格式：yyyy-MM-dd HH:mm:ss **/
	private static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	/** 日期格式：yyyy-MM-dd HH:mm **/
	private static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	/** 日期格式：yyyy-MM-dd **/
	private static final String DF_YYYY_MM_DD = "yyyy-MM-dd";
    /** 日期格式：MM-dd HH:mm**/
    private static final String DF_MM_DD_HH_MM = "MM-dd HH:mm";
    /** 日期格式：MM-dd **/
    private static final String DF_MM_DD = "MM-dd";


	/** 日期格式：HH:mm:ss **/
	private static final String DF_HH_MM_SS = "HH:mm:ss";
	/** 日期格式：HH:mm **/
	private static final String DF_HH_MM = "HH:mm";

	/** 日期格式：yyyy年MM月dd日 HH时mm分ss秒 **/
	private static String DF_C_YYYY_MM_DD_HH_MM_SS = "yyyy年MM月dd日 HH时mm分ss秒";
	/** 日期格式：yyyy年MM月dd日 HH时mm分 **/
	private static String DF_C_YYYY_MM_DD_HH_MM="yyyy年MM月dd日 HH时mm分";
	/** 日期格式：yyyy年MM月dd日 HH时 **/
	private static String DF_C_YYYY_MM_DD_HH="yyyy年MM月dd日 HH时";
	/** 日期格式：yyyy年MM月dd日 **/
	private static String DF_C_YYYY_MM_DD="yyyy年MM月dd日";
    /** 日期格式：MM月dd日HH时mm分**/
    private static final String DF_C_MM_DD_HH_MM = "MM月dd日HH时mm分";
    /** 日期格式：MM月dd日 **/
    private static final String DF_C_MM_DD = "MM月dd日";
	/** 日期格式：时 分秒 **/
    private static final String DF_C_HH_MM_SS = "HH时mm分ss秒";
	/** 日期格式：时 分 **/
    private static final String DF_C_HH_MM = "HH时mm 分";

    private static final String DATE_FORMAT = "%4d-%02d-%02d";

    /**
     * 日期格式枚举
     */
    public enum DateType {
        DF_YYYY_MM_DD_HH_MM_SS,
        DF_YYYY_MM_DD_HH_MM,
        DF_YYYY_MM_DD,
        DF_MM_DD_HH_MM,
        DF_MM_DD,
        DF_HH_MM_SS,
        DF_HH_MM,
        DF_C_YYYY_MM_DD_HH_MM_SS,
        DF_C_YYYY_MM_DD_HH_MM,
        DF_C_YYYY_MM_DD_HH,
        DF_C_YYYY_MM_DD,
        DF_C_MM_DD_HH_MM,
        DF_C_MM_DD,
        DF_C_HH_MM_SS,
        DF_C_HH_MM;
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
