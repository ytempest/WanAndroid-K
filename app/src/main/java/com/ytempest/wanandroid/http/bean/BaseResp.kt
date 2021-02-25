package com.ytempest.wanandroid.http.bean

/**
 * @author heqidu
 * @since 21-2-8
 */
class BaseResp<T> {

    // 0：成功，1：失败
    private var errorCode = 0

    private var errorMsg: String? = null

    private var data: T? = null

    fun getErrorCode(): Int {
        return errorCode
    }

    fun setErrorCode(errorCode: Int) {
        this.errorCode = errorCode
    }

    fun getErrorMsg(): String? {
        return errorMsg
    }

    fun setErrorMsg(errorMsg: String?) {
        this.errorMsg = errorMsg
    }

    fun getData(): T? {
        return data
    }

    fun setData(data: T) {
        this.data = data
    }

    /*Ext*/
    val SUCCESS = 0
    val FAIL = 1

    /**
     * 判断请求是否成功
     */
    fun isSuccess(): Boolean {
        return errorCode == SUCCESS
    }
}