package com.ytempest.wanandroid.activity.architecture;

import com.ytempest.wanandroid.base.presenter.IPresenter;
import com.ytempest.wanandroid.base.view.IView;

/**
 * @author ytempest
 * @since 2021/1/6
 */
public interface IArchitectureContract {
    interface View extends IView {
    }

    interface Presenter extends IPresenter {
    }
}
