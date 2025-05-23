package com.ytempest.tool.util;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.widget.Toast;

/**
 * @author ytempest
 * @since 2020/2/27
 */
public class ToastUtils {
    public static Toast show(Context context, CharSequence text) {
        return show(context, text, Toast.LENGTH_SHORT);
    }

    public static Toast show(Context context, @StringRes int textId) {
        return show(context, textId, Toast.LENGTH_SHORT);
    }

    public static Toast showLong(Context context, CharSequence text) {
        return show(context, text, Toast.LENGTH_LONG);
    }

    public static Toast showLong(Context context, @StringRes int textId) {
        return show(context, textId, Toast.LENGTH_LONG);
    }

    @Nullable
    public static Toast show(Context context, @StringRes int textId, int duration) {
        if (context == null) {
            return null;
        }
        String text = context.getString(textId);
        return show(context, text, duration);
    }

    @Nullable
    public static Toast show(Context context, CharSequence text, int duration) {
        Toast toast = null;
        if (context != null) {
            try {
                toast = Toast.makeText(context, text, duration);
                toast.show();
            } catch (Exception e) {
            }
        }
        return toast;
    }
}
