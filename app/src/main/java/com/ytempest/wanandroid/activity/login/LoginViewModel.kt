package com.ytempest.wanandroid.activity.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ytempest.wanandroid.base.vm.BaseViewModel
import com.ytempest.wanandroid.http.bean.Entity
import com.ytempest.wanandroid.http.bean.LoginBean
import com.ytempest.wanandroid.http.bean.NegativeEntity
import com.ytempest.wanandroid.http.bean.PositiveEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author ytempest
 * @since 2021/8/9
 */
class LoginViewModel(application: Application) : BaseViewModel(application) {


    val loginResult = MutableLiveData<Entity<LoginBean>>()

    fun login(account: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            request(mInteractor.httpHelper.login(account, password),
                    onSuccess = { data ->
                        val config = mInteractor.configs.getUser()
                        config.setAccount(data.username)
                        config.setUserLoginStatus(true)
                        loginResult.postValue(PositiveEntity(data))
                    },
                    onFail = { code, throwable ->
                        loginResult.postValue(NegativeEntity(code, throwable))
                    })
        }

    }

}