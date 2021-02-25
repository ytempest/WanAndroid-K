package com.ytempest.wanandroid.http

import androidx.annotation.IntDef


/**
 * @author heqidu
 * @since 21-2-7
 */


@IntDef(
        ErrCode.SRC_ERR,
        ErrCode.NET_ERR,
        ErrCode.EMPTY_RESP,
        ErrCode.DATA_ERR,
        ErrCode.REQUEST_ERR,
        ErrCode.UNKNOWN_ERR,
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class ErrCode {
    companion object {
        const val SRC_ERR = -1 // 服务器返回的错误
        const val NET_ERR = 1
        const val EMPTY_RESP = 2
        const val DATA_ERR = 3
        const val REQUEST_ERR = 4
        const val UNKNOWN_ERR = 5
    }
}



