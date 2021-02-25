package com.ytempest.wanandroid.base.presenter

import com.ytempest.wanandroid.base.view.IView

/**
 * @author heqidu
 * @since 21-2-7
 */
interface IPresenter {

    fun <V : IView> attachView(view: V)

    fun detach()
}