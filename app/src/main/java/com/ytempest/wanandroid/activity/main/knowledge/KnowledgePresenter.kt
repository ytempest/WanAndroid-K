package com.ytempest.wanandroid.activity.main.knowledge

import com.ytempest.wanandroid.base.presenter.BasePresenter
import com.ytempest.wanandroid.http.observer.HandlerObserver
import com.ytempest.wanandroid.interactor.impl.BaseInteractor
import com.ytempest.wanandroid.utils.RxUtils
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-9
 */
class KnowledgePresenter @Inject constructor(
        interactor: BaseInteractor
) : BasePresenter<IKnowledgeView>(interactor), IKnowledgePresenter {

    override fun getKnowledgeArchitecture() {
        mInteractor.getHttpHelper().getKnowledgeArchitecture()
                .compose(RxUtils.switchScheduler())
                .subscribe(object : HandlerObserver<List<KnowledgeArchitectureBean>>(mView) {
                    override fun onSuccess(list: List<KnowledgeArchitectureBean>) {
                        mView.onKnowledgeArchitectureReceived(list)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mView.onKnowledgeArchitectureFail(code)
                    }
                })
    }

}