package com.ytempest.wanandroid.activity.register

import com.ytempest.wanandroid.base.view.IView
import com.ytempest.wanandroid.http.ErrCode
import com.ytempest.wanandroid.http.bean.LoginBean

/**
 * @author heqidu
 * @since 21-2-22
 */
interface IRegisterView : IView {
    fun onRegisterSuccess(loginBean: LoginBean)

    fun onRegisterFail(@ErrCode errCode: Int, throwable: Throwable?)
}