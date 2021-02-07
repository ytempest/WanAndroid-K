package com.ytempest.wanandroid.activity.register;

import com.ytempest.wanandroid.base.presenter.IPresenter;
import com.ytempest.wanandroid.base.view.IView;
import com.ytempest.wanandroid.http.ErrCode;
import com.ytempest.wanandroid.http.bean.LoginBean;

/**
 * @author heqidu
 * @since 2020/8/14
 */
public interface IRegisterContract {
    interface View extends IView {
        void onRegisterSuccess(LoginBean loginBean);

        void onRegisterFail(@ErrCode int errCode, Throwable throwable);
    }

    interface Presenter extends IPresenter {
        void register(String account, String pwd, String confirmPwd);
    }

}
