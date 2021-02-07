package com.ytempest.wanandroid.http.bean;

/**
 * @author heqidu
 * @since 2020/7/4
 */
public class BaseResp<T> {
    // 0：成功，1：失败
    private int errorCode;

    private String errorMsg;

    private T data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /*Ext*/

    public static final int SUCCESS = 0;
    public static final int FAIL = 1;

    /**
     * 判断请求是否成功
     */
    public boolean isSuccess() {
        return errorCode == SUCCESS;
    }
}
