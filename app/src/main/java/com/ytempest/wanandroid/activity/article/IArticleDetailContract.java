package com.ytempest.wanandroid.activity.article;

import com.ytempest.wanandroid.base.presenter.IPresenter;
import com.ytempest.wanandroid.base.view.IView;

/**
 * @author heqidu
 * @since 2020/12/15
 */
public interface IArticleDetailContract {
    interface View extends IView {
        void onArticleCollectSuccess(boolean isCollect, long articleId);

        void onArticleCollectFail(boolean isCollect, long articleId, int errCode);
    }

    interface Presenter extends IPresenter {
        boolean isUserLogin();

        void updateArticleCollectStatus(boolean isCollected, long articleId);
    }
}
