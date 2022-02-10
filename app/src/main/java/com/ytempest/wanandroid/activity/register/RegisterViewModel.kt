package com.ytempest.wanandroid.activity.register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ytempest.wanandroid.base.vm.BaseViewModel
import com.ytempest.wanandroid.http.bean.Entity
import com.ytempest.wanandroid.http.bean.LoginBean
import com.ytempest.wanandroid.http.bean.NegativeEntity
import com.ytempest.wanandroid.http.bean.PositiveEntity

/**
 * @author qiduhe
 * @since 2021/8/10
 */
class RegisterViewModel(application: Application) : BaseViewModel(application) {

    val registerResult = MutableLiveData<Entity<LoginBean>>()

    fun register(account: String, pwd: String, confirmPwd: String) {
        request(mInteractor.httpHelper.register(account, pwd, confirmPwd),
                onSuccess = { result ->
                    registerResult.postValue(PositiveEntity(result))
                },
                onFail = { code, throwable ->
                    registerResult.postValue(NegativeEntity(code, throwable))
                })
    }
}