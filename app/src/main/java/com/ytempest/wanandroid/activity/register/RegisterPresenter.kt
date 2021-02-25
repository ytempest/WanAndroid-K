package com.ytempest.wanandroid.activity.register

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
class RegisterPresenter @Inject constructor(
        interactor: BaseInteractor
) : BasePresenter<IRegisterView>(interactor), IRegisterPresenter {

    override fun register(account: String, pwd: String, confirmPwd: String) {
        mInteractor.getHttpHelper().register(account, pwd, confirmPwd)
                .compose(RxUtils.switchScheduler())
                .subscribe(object : HandlerObserver<LoginBean>(mView) {
                    override fun onSuccess(loginBean: LoginBean) =
                            mView.onRegisterSuccess(loginBean)

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mView.onRegisterFail(code, e)
                    }
                })
    }
}