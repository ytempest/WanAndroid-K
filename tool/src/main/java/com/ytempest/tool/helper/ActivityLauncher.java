package com.ytempest.tool.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author heqidu
 * @since 2020/9/29
 */
public class ActivityLauncher {

    public static void startActivity(Context context, Class<? extends Activity> activity) {
        startActivity(context, new Intent(context, activity));
    }

    public static void startActivity(Context context, Intent intent) {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}
