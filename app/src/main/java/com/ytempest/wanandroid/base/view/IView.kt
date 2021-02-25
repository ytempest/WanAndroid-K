package com.ytempest.wanandroid.base.view

import androidx.annotation.StringRes

/**
 * @author heqidu
 * @since 21-2-7
 */
interface IView {

    fun showToast(msg: String?)

    fun showToast(@StringRes textId: Int)

    fun showLoading()

    fun stopLoading()
}