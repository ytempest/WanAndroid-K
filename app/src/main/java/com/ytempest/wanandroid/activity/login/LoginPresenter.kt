package com.ytempest.wanandroid.activity.login

import com.ytempest.wanandroid.base.presenter.BasePresenter
import com.ytempest.wanandroid.http.observer.HandlerObserver
import com.ytempest.wanandroid.interactor.impl.BaseInteractor
import com.ytempest.wanandroid.utils.RxUtils
import com.ytempest.wanandroid.http.bean.LoginBean

import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-22
 */
class LoginPresenter @Inject constructor(
        interactor: BaseInteractor
) : BasePresenter<ILoginView>(interactor), ILoginPresenter {

    override fun login(account: String, password: String) {
        mInteractor.getHttpHelper().login(account, password)
                .compose(RxUtils.switchScheduler())
                .subscribe(object : HandlerObserver<LoginBean>(mView) {
                    override fun onSuccess(loginBean: LoginBean) {
                        val config = mInteractor.getConfigs().getUser()
                        config.setAccount(loginBean.username)
                        config.setUserLoginStatus(true)
                        mView.onLoginSuccess(loginBean)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mView.onLoginFail(code, e)
                    }
                })
    }
}