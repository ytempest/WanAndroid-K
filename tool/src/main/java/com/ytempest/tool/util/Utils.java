package com.ytempest.tool.util;

/**
 * @author heqidu
 * @since 2020/3/2
 */
public class Utils {

    private static long sLastClickTime;

    public static boolean isQuickClick() {
        long curTime = System.currentTimeMillis();
        boolean isQuick = (curTime - sLastClickTime < 700);
        if (!isQuick) {
            sLastClickTime = curTime;
        }
        return isQuick;
    }
}
