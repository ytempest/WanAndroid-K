package com.ytempest.wanandroid.base.presenter;

import com.ytempest.wanandroid.base.view.IView;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public interface IPresenter {

    <V extends IView> void attachView(V view);

    void detach();
}
