package com.ytempest.wanandroid.interactor.configs

import com.ytempest.wanandroid.base.WanApp
import com.ytempest.wanandroid.interactor.configs.base.AbsConfig
import com.ytempest.wanandroid.interactor.configs.base.PreferencesName

/**
 * @author heqidu
 * @since 21-2-8
 */

class UserConfig : AbsConfig(WanApp.instance, PreferencesName.USER) {

    private val KEY_USER_ACCOUNT = "user_account"
    private val KEY_USER_LOGIN_STATUS = "user_login_status"

    fun setAccount(account: String) = mPref.putVal(KEY_USER_ACCOUNT, account)
    fun getAccount(): String = mPref.getVal(KEY_USER_ACCOUNT, "")
    fun clearAccount() = mPref.putVal(KEY_USER_ACCOUNT, "")

    fun isUserLogin(): Boolean = mPref.getVal(KEY_USER_LOGIN_STATUS, false)
    fun setUserLoginStatus(isLogin: Boolean) = mPref.putVal(KEY_USER_LOGIN_STATUS, isLogin)

}