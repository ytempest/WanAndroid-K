package com.ytempest.wanandroid.activity.main

import com.ytempest.wanandroid.base.presenter.BasePresenter
import com.ytempest.wanandroid.interactor.impl.BaseInteractor
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-8
 */
class MainPresenter @Inject constructor(
        interactor: BaseInteractor
) : BasePresenter<IMainView>(interactor), IMainPresenter {

}