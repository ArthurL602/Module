package com.ljb.utils.utils.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class TimeConstants {

    public static final int MSEC = 1; //毫秒
    public static final int SEC  = 1000; //秒
    public static final int MIN  = 60000; // 分钟
    public static final int HOUR = 3600000; //小时
    public static final int DAY  = 86400000; //天

    @IntDef({MSEC, SEC, MIN, HOUR, DAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }
}