package com.ytempest.wanandroid.http.observer

/**
 * @author heqidu
 * @since 21-2-7
 */
class DataErrException constructor(errMsg: String?)
    : Exception(errMsg) {

    constructor() : this(null) {
    }

}
