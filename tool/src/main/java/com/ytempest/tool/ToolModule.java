package com.ytempest.tool;

import android.content.Context;

import com.ytempest.tool.util.DrawUtils;

/**
 * @author heqidu
 * @since 2020/6/20
 */
public class ToolModule {
    public static void init(Context context) {
        DrawUtils.resetDensity(context);
    }
}
