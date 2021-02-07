package com.ytempest.wanandroid.activity.login;

import com.ytempest.wanandroid.base.presenter.IPresenter;
import com.ytempest.wanandroid.base.view.IView;
import com.ytempest.wanandroid.http.ErrCode;
import com.ytempest.wanandroid.http.bean.LoginBean;

/**
 * @author heqidu
 * @since 2020/8/11
 */
public interface ILoginContract {
    interface View extends IView {
        void onLoginSuccess(LoginBean loginBean);

        void onLoginFail(@ErrCode int code, Throwable throwable);
    }

    interface Presenter extends IPresenter {
        void login(String account, String password);
    }

}
