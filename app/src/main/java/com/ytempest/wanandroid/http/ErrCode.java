package com.ytempest.wanandroid.http;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author heqidu
 * @since 2020/8/13
 */
@IntDef({
        ErrCode.SRC_ERR,
        ErrCode.NET_ERR,
        ErrCode.EMPTY_RESP,
        ErrCode.DATA_ERR,
        ErrCode.REQUEST_ERR,
        ErrCode.UNKNOWN_ERR,
})
@Retention(RetentionPolicy.SOURCE)
public @interface ErrCode {
    int SRC_ERR = -1; // 服务器返回的错误
    int NET_ERR = 1;
    int EMPTY_RESP = 2;
    int DATA_ERR = 3;
    int REQUEST_ERR = 4;
    int UNKNOWN_ERR = 5;
}
