package com.ytempest.wanandroid.activity.login

import com.ytempest.wanandroid.base.view.IView
import com.ytempest.wanandroid.http.ErrCode
import com.ytempest.wanandroid.http.bean.LoginBean

/**
 * @author heqidu
 * @since 21-2-22
 */
interface ILoginView : IView {
    fun onLoginSuccess(loginBean: LoginBean)

    fun onLoginFail(@ErrCode code: Int, throwable: Throwable?)
}