package com.ytempest.wanandroid.activity.main.home;

import com.ytempest.wanandroid.base.presenter.IPresenter;
import com.ytempest.wanandroid.base.view.IView;
import com.ytempest.wanandroid.http.bean.BannerBean;
import com.ytempest.wanandroid.http.bean.HomeArticleBean;

import java.util.List;

/**
 * @author heqidu
 * @since 2020/6/30
 */
public interface IHomeContract {
    interface View extends IView {
        void displayHomeArticle(boolean fromRefresh, HomeArticleBean homeArticleBean);

        void onArticleCollectSuccess(HomeArticleBean.DatasBean data);

        void onArticleCollectFail(HomeArticleBean.DatasBean data, int code);

        void onHomeDataSuccess(List<BannerBean> bannerList, HomeArticleBean bean);

        void onHomeDataFail(int code);

    }

    interface Presenter extends IPresenter {
        boolean isUserLogin();

        void loadHomeData();

        void refreshHomeArticle();

        void loadMoreHomeArticle();

        void updateArticleCollectStatus(HomeArticleBean.DatasBean data);
    }
}
