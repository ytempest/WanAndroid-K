package com.ytempest.wanandroid.activity.main.project;

import com.ytempest.wanandroid.base.presenter.IPresenter;
import com.ytempest.wanandroid.base.view.IView;
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean;

import java.util.List;

/**
 * @author heqidu
 * @since 2020/6/30
 */
public interface IProjectContract {
    interface View extends IView {

        void onProjectClassifyReceived(List<ProjectClassifyBean> list);

        void onProjectClassifyFail(int code);
    }

    interface Presenter extends IPresenter {
        void getProjectClassify();
    }
}
