package com.ytempest.tool.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * @author heqidu
 * @since 2020/8/12
 */
public class RegexUtils {
    private static final Pattern PHONE_REGEX = Pattern.compile("^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    private static final Pattern EMAIL_REGEX = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

    public static boolean isPhone(String phone) {
        return isMatch(PHONE_REGEX, phone);
    }

    public static boolean isEmail(String email) {
        return isMatch(EMAIL_REGEX, email);
    }

    public static boolean isMatch(Pattern regex, String str) {
        return !TextUtils.isEmpty(str) && regex.matcher(str).matches();
    }
}
