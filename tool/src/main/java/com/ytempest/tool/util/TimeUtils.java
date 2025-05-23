package com.ytempest.tool.util;

import java.util.concurrent.TimeUnit;

/**
 * @author heqidu
 * @since 2020/2/26
 */
public class TimeUtils {

    public static boolean isSameDay(long first, long second) {
        return Math.abs(first - second) < TimeUnit.DAYS.toMillis(1);
    }
}
