package com.ytempest.wanandroid.http.observer

import android.util.Log
import androidx.annotation.NonNull
import com.ytempest.wanandroid.http.ErrCode
import com.ytempest.wanandroid.http.bean.BaseResp
import io.reactivex.observers.DefaultObserver
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author heqidu
 * @since 21-2-7
 */
abstract class BaseObserver<T> : DefaultObserver<BaseResp<T>>() {

    private val TAG = "BaseObserver"

    override fun onNext(resp: BaseResp<T>) {
        Log.d(TAG, "onNext: ");
        if (resp == null) {
            onFail(ErrCode.EMPTY_RESP, DataErrException())
            return
        }

        if (!resp.isSuccess()) {
            onFail(ErrCode.SRC_ERR, DataErrException(resp.getErrorMsg()))
            return
        }

        val data = onHookData(resp.getData())
        if (data == null) {
            onFail(ErrCode.DATA_ERR, DataErrException(resp.getErrorMsg()))
            return
        }

        onSuccess(data)

    }

    override fun onError(e: Throwable) {
        Log.d(TAG, "onError: ");
        when (e) {
            is HttpException, is UnknownHostException -> onFail(ErrCode.NET_ERR, e)
            is SocketTimeoutException -> onFail(ErrCode.REQUEST_ERR, e)
            else -> onFail(ErrCode.UNKNOWN_ERR, e)
        }
    }

    override fun onComplete() {
    }

    protected open fun onHookData(data: T?): T? = data

    protected abstract fun onSuccess(@NonNull t: T)

    protected abstract fun onFail(@ErrCode code: Int, e: Throwable)
}