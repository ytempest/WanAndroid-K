package com.ytempest.tool.util;

import android.util.Log;

/**
 * @author heqidu
 * @since 2020/3/11
 */
public class LogUtils {
    private static boolean LOGGABLE;
    private static String PREFIX = "";

    public static void setLogPrefix(String prefix) {
        PREFIX = prefix;
    }

    public static void setLoggable(boolean loggable) {
        LOGGABLE = loggable;
    }

    public static boolean isLoggable() {
        return LOGGABLE;
    }

    public static void v(String tag, String msg) {
        if (LOGGABLE) {
            Log.d(PREFIX + tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOGGABLE) {
            Log.d(PREFIX + tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LOGGABLE) {
            Log.i(PREFIX + tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LOGGABLE) {
            Log.w(PREFIX + tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LOGGABLE) {
            Log.e(PREFIX + tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (LOGGABLE) {
            Log.d(PREFIX + tag, msg, throwable);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (LOGGABLE) {
            Log.e(PREFIX + tag, msg, throwable);
        }
    }
}
