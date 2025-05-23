package com.ytempest.tool.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author heqidu
 * @since 2020/6/20
 */
public class DrawUtils {

    private static int sWidthPixels;
    private static int sHeightPixels;
    private static float sDensity;
    private static float sScaledDensity;

    public static void resetDensity(Context context) {
        DisplayMetrics outMetrics = context.getResources().getDisplayMetrics();
        sWidthPixels = outMetrics.widthPixels;
        sHeightPixels = outMetrics.heightPixels;
        sDensity = outMetrics.density;
        sScaledDensity = outMetrics.scaledDensity;
    }

    public static int getScreenWidth() {
        return sWidthPixels;
    }

    public static int getScreenHeight() {
        return sHeightPixels;
    }

    public static int dp2px(float dp) {
        return dp >= 0 ? (int) (dp * sDensity - 0.5F) : (int) (dp * sDensity + 0.5F);
    }

    public static int sp2px(float sp) {
        return (int) (sScaledDensity * sp);
    }

    public static int px2dp(float px) {
        return (int) (px / sDensity + 0.5f);
    }

    public static int px2sp(float px) {
        return (int) (px / sScaledDensity);
    }
}
