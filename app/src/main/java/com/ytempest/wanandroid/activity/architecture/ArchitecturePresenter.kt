package com.ytempest.wanandroid.activity.architecture

import com.ytempest.wanandroid.base.presenter.BasePresenter
import com.ytempest.wanandroid.interactor.impl.BaseInteractor
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-22
 */
class ArchitecturePresenter @Inject constructor(
        interactor: BaseInteractor
) : BasePresenter<IArchitectureView>(interactor), IArchitecturePresenter {
}