package com.ytempest.tool.util;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * @author heqidu
 * @since 2020/3/7
 */
public class ViewUtils {

    public static void addUnderline(TextView... textViews) {
        for (int i = 0, len = DataUtils.getSize(textViews); i < len; i++) {
            TextPaint paint = textViews[i].getPaint();
            paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
            paint.setAntiAlias(true);
        }
    }

    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
