package com.ytempest.tool.callback;

import androidx.annotation.Nullable;

/**
 * @author heqidu
 * @since 2020/2/22
 */
public class SimpleCallback {

    /*Callback*/

    public static <Param> void call(Callback<Param> callback) {
        call(callback, null);
    }

    public static <Param> void call(Callback<Param> callback, @Nullable Param param) {
        if (callback != null) {
            callback.onCall(param);
        }
    }

    /*ParamsCallback*/

    public static <First, Second> void call(ParamsCallback<First, Second> callback) {
        call(callback, null, null);
    }

    public static <First, Second> void call(ParamsCallback<First, Second> callback, First first, Second second) {
        if (callback != null) {
            callback.onCall(first, second);
        }
    }

    /*ResultCallback*/

    @Nullable
    public static <Param, Result> Result call(ResultCallback<Param, Result> callback, Param param) {
        return callback != null ? callback.onCall(param) : null;
    }
}
