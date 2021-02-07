package com.ytempest.wanandroid.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * @author heqidu
 * @since 2020/8/14
 * TextView的空格过滤器
 */
public class SpaceInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        return source.toString().equals(" ") ? "" : null;
    }
}
