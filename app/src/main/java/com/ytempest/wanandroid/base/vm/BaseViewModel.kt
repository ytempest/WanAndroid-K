package com.ytempest.wanandroid.base.vm

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.ytempest.tool.util.LogUtils
import com.ytempest.wanandroid.http.ErrCode
import com.ytempest.wanandroid.http.bean.BaseResp
import com.ytempest.wanandroid.interactor.impl.BaseInteractorK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import retrofit2.Call
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author ytempest
 * @since 2021/8/9
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope by MainScope() {

    val mInteractor = BaseInteractorK

    @WorkerThread
    fun <T> request(call: Call<BaseResp<T>>,
                    onSuccess: ((T) -> Unit)? = null,
                    onFail: ((Int, Throwable?) -> Unit)? = null
    ) {

        try {
            val resp: BaseResp<T>? = call.execute()?.body()
            if (resp == null) {
                onFail?.invoke(ErrCode.EMPTY_RESP, null)
                return
            }

            val data = resp.getData()
            if (data == null) {
                onFail?.invoke(ErrCode.DATA_ERR, null)
                return
            }

            onSuccess?.invoke(data)
        } catch (e: Exception) {
            if (LogUtils.isLoggable()) e.printStackTrace()
            when (e) {
                is HttpException, is UnknownHostException -> onFail?.invoke(ErrCode.NET_ERR, e)
                is SocketTimeoutException -> onFail?.invoke(ErrCode.REQUEST_ERR, e)
                else -> onFail?.invoke(ErrCode.UNKNOWN_ERR, e)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}