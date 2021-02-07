package com.ytempest.wanandroid.interactor.configs;

import com.ytempest.wanandroid.base.WanApp;
import com.ytempest.wanandroid.interactor.configs.base.AbsConfig;
import com.ytempest.wanandroid.interactor.configs.base.PreferencesName;

/**
 * @author heqidu
 * @since 2020/8/15
 */
public class UserConfig extends AbsConfig {

    UserConfig() {
        super(WanApp.getApp(), PreferencesName.USER);
    }

    private static final String KEY_USER_ACCOUNT = "user_account";
    private static final String KEY_USER_LOGIN_STATUS = "user_login_status";

    public void setAccount(String account) {
        mPref.putString(KEY_USER_ACCOUNT, account);
    }

    public String getAccount() {
        return mPref.getString(KEY_USER_ACCOUNT, "");
    }

    public void clearAccount() {
        mPref.putString(KEY_USER_ACCOUNT, "");
    }

    public boolean isUserLogin() {
        return mPref.getBoolean(KEY_USER_LOGIN_STATUS, false);
    }

    public void setUserLoginStatus(boolean isLogin) {
        mPref.putBoolean(KEY_USER_LOGIN_STATUS, isLogin);
    }
}
