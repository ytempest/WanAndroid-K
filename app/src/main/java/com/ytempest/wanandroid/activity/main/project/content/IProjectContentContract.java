package com.ytempest.wanandroid.activity.main.project.content;

import com.ytempest.wanandroid.base.presenter.IPresenter;
import com.ytempest.wanandroid.base.view.IView;
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean;
import com.ytempest.wanandroid.http.bean.ProjectContentBean;

/**
 * @author heqidu
 * @since 2020/12/25
 */
public interface IProjectContentContract {
    interface View extends IView {
        void displayProjectContent(ProjectContentBean projectContent);

        void onProjectContentFail(int code);

        void onMoreProjectContentLoaded(ProjectContentBean projectContent);

        void onProjectArticleCollectSuccess(ProjectContentBean.DatasBean bean);

        void onProjectArticleCollectFail(int code, boolean onceCollected, ProjectContentBean.DatasBean bean);
    }

    interface Presenter extends IPresenter {
        void refreshContent(ProjectClassifyBean classifyBean);

        void loadMoreProjectContent(ProjectClassifyBean classifyBean);

        void addProjectArticleCollect(ProjectContentBean.DatasBean bean);
    }
}
