package com.ytempest.tool.util;

import android.content.Intent;

/**
 * @author heqidu
 * @since 2020/10/2
 */
public class IntentUtils {
    public static int getInt(Intent intent, String key, int defVal) {
        return intent != null ? intent.getIntExtra(key, defVal) : defVal;
    }

    public static String getString(Intent intent, String key, String defVal) {
        return intent != null ? intent.getStringExtra(key) : defVal;
    }

    public static float getFloat(Intent intent, String key, float defVal) {
        return intent != null ? intent.getFloatExtra(key, defVal) : defVal;
    }

    public static long getLong(Intent intent, String key, long defVal) {
        return intent != null ? intent.getLongExtra(key, defVal) : defVal;
    }

    public static boolean getBoolean(Intent intent, String key, boolean defVal) {
        return intent != null ? intent.getBooleanExtra(key, defVal) : defVal;
    }
}
