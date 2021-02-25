package com.ytempest.wanandroid.activity.main.project

import com.ytempest.wanandroid.base.presenter.BasePresenter
import com.ytempest.wanandroid.http.observer.HandlerObserver
import com.ytempest.wanandroid.interactor.impl.BaseInteractor
import com.ytempest.wanandroid.utils.RxUtils
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-10
 */
class ProjectPresenter @Inject constructor(
        interactor: BaseInteractor
) : BasePresenter<IProjectView>(interactor), IProjectPresenter {

    override fun getProjectClassify() {
        mInteractor.getHttpHelper().getProjectClassify()
                .compose(RxUtils.switchScheduler())
                .subscribe(object : HandlerObserver<List<ProjectClassifyBean>>(mView) {
                    override fun onSuccess(list: List<ProjectClassifyBean>) {
                        mView.onProjectClassifyReceived(list)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mView.onProjectClassifyFail(code)
                    }
                })
    }
}