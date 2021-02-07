package com.ytempest.wanandroid.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author heqidu
 * @since 2020/12/17
 */
public class DateFormat {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm", Locale.CHINA);

    public static String format(long time) {
        return format(new Date(time));
    }

    public static String format(Date time) {
        return FORMAT.format(time);
    }
}
