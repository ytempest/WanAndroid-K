package com.ytempest.wanandroid.http.observer;

/**
 * @author heqidu
 * @since 2020/7/4
 */
public class DataErrException extends Exception {

    public DataErrException() {
        this(null);
    }

    public DataErrException(String errorMsg) {
        super(errorMsg);
    }
}
