package com.ytempest.wanandroid.activity.architecture;

import com.ytempest.wanandroid.base.presenter.BasePresenter;
import com.ytempest.wanandroid.interactor.impl.BaseInteractor;

import javax.inject.Inject;

/**
 * @author ytempest
 * @since 2021/1/6
 */
public class ArchitecturePresenter extends BasePresenter<IArchitectureContract.View> implements IArchitectureContract.Presenter {

    @Inject
    public ArchitecturePresenter(BaseInteractor interactor) {
        super(interactor);
    }
}
