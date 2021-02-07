package com.ytempest.wanandroid.activity.main;

import com.ytempest.wanandroid.base.presenter.BasePresenter;
import com.ytempest.wanandroid.interactor.impl.BaseInteractor;

import javax.inject.Inject;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public class MainPresenter extends BasePresenter<IMainContract.View> implements IMainContract.Presenter {

    @Inject
    public MainPresenter(BaseInteractor interactor) {
        super(interactor);
    }
}
