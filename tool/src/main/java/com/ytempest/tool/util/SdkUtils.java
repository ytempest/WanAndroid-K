package com.ytempest.tool.util;

import android.os.Build;

/**
 * @author heqidu
 * @since 2020/9/29
 */
public class SdkUtils {

    public static boolean OVER_JELLY_BEAN = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;

    public static boolean OVER_JELLY_BEAN_MR1 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;

    public static boolean OVER_O = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;

    public static boolean OVER_N = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;

    public static boolean OVER_M = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

    public static boolean OVER_LOLLIPOP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    public static boolean OVER_KITKAT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
}
