package com.ytempest.wanandroid.base.presenter;

import com.ytempest.wanandroid.base.view.IView;
import com.ytempest.wanandroid.interactor.impl.BaseInteractor;

import java.lang.reflect.Proxy;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public class BasePresenter<View extends IView> implements IPresenter {

    private View mSrcView;
    protected View mView;
    protected final BaseInteractor mInteractor;

    public BasePresenter(BaseInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public <V extends IView> void attachView(V view) {
        mSrcView = (View) view;
        mView = (View) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    if (mSrcView != null) {
                        return method.invoke(mSrcView, args);
                    }
                    return null;
                });
    }

    @Override
    public void detach() {
        mSrcView = null;
    }
}
