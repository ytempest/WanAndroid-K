package com.ytempest.wanandroid.activity.architecture.content;

import com.ytempest.wanandroid.base.presenter.IPresenter;
import com.ytempest.wanandroid.base.view.IView;
import com.ytempest.wanandroid.http.bean.ArchitectureContentBean;

/**
 * @author ytempest
 * @since 2021/1/6
 */
public interface IArchArticleContract {
    interface View extends IView {
        void onLoadArchitectureContent(ArchitectureContentBean architectureContentBean, boolean fromRefresh);

        void onArchArticleCollectSuccess(boolean isCollect, ArchitectureContentBean.DatasBean article);

        void onArchArticleCollectFail(boolean isCollect, ArchitectureContentBean.DatasBean article, int code);

        void onRefreshArchitectureFail(int code);
    }

    interface Presenter extends IPresenter {
        boolean isUserLogin();

        void refreshArchitectureContent(int id);

        void loadMoreArchitectureContent(int id);

        void updateArticleCollectStatus(boolean isCollect, ArchitectureContentBean.DatasBean article);
    }
}
