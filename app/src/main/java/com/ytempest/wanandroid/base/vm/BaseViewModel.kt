package com.ytempest.wanandroid.base.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ytempest.tool.util.LogUtils
import com.ytempest.wanandroid.http.ErrCode
import com.ytempest.wanandroid.http.bean.BaseResp
import com.ytempest.wanandroid.interactor.impl.BaseInteractorK
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author ytempest
 * @since 2021/8/9
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope by MainScope() {

    val mInteractor = BaseInteractorK

    fun <T> request(
        call: Call<BaseResp<T>>,
        responseHook: ((BaseResp<T>) -> Unit)? = null,
        onSuccess: ((T) -> Unit)? = null,
        onFail: ((Int, Throwable?) -> Unit)? = null,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp: BaseResp<T>? = call.execute()?.body()
                launch(Dispatchers.Main) {
                    if (resp == null) {
                        onFail?.invoke(ErrCode.EMPTY_RESP, null)
                        return@launch
                    }

                    // 拦截
                    responseHook?.invoke(resp)

                    if (!resp.isSuccess()) {
                        onFail?.invoke(ErrCode.SRC_ERR, null)
                        return@launch
                    }

                    val data = resp.getData()
                    if (data == null) {
                        onFail?.invoke(ErrCode.DATA_ERR, null)
                        return@launch
                    }

                    onSuccess?.invoke(data)
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    if (LogUtils.isLoggable()) e.printStackTrace()
                    when (e) {
                        is HttpException, is UnknownHostException -> onFail?.invoke(ErrCode.NET_ERR, e)
                        is SocketTimeoutException -> onFail?.invoke(ErrCode.REQUEST_ERR, e)
                        else -> onFail?.invoke(ErrCode.UNKNOWN_ERR, e)
                    }
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}