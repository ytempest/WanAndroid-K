package com.ytempest.wanandroid.utils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

/**
 * @author heqidu
 * @since 2020/12/16
 */
public class JSON {
    private static final Gson GSON = new Gson();

    @Nullable
    public static <T> T from(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static <T> String toJson(T t) {
        return GSON.toJson(t);
    }

}
