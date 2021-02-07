package com.ytempest.wanandroid.interactor.impl;

import com.ytempest.wanandroid.http.HttpApi;
import com.ytempest.wanandroid.http.bean.ArchitectureContentBean;
import com.ytempest.wanandroid.http.bean.ArticleCollectBean;
import com.ytempest.wanandroid.http.bean.BannerBean;
import com.ytempest.wanandroid.http.bean.BaseResp;
import com.ytempest.wanandroid.http.bean.HomeArticleBean;
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean;
import com.ytempest.wanandroid.http.bean.LoginBean;
import com.ytempest.wanandroid.http.bean.MyCollectionBean;
import com.ytempest.wanandroid.http.bean.NavigationListBean;
import com.ytempest.wanandroid.http.bean.OutsideArticleCollectBean;
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean;
import com.ytempest.wanandroid.http.bean.ProjectContentBean;
import com.ytempest.wanandroid.interactor.HttpHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public class HttpHelperImpl implements HttpHelper {

    private final HttpApi mHttpApi;

    @Inject
    public HttpHelperImpl(HttpApi httpApi) {
        mHttpApi = httpApi;
    }

    @Override
    public Observable<BaseResp<HomeArticleBean>> getHomeArticleList(int pageNum) {
        return mHttpApi.getHomeArticleList(pageNum);
    }

    @Override
    public Observable<BaseResp<LoginBean>> login(String account, String password) {
        return mHttpApi.login(account, password);
    }

    @Override
    public Observable<BaseResp<LoginBean>> register(String account, String pwd, String confirmPwd) {
        return mHttpApi.register(account, pwd, confirmPwd);
    }

    @Override
    public Observable<BaseResp<List<BannerBean>>> getBannerList() {
        return mHttpApi.getBannerList();
    }

    @Override
    public Observable<BaseResp<MyCollectionBean>> getMyCollectionList() {
        return mHttpApi.getMyCollectionList();
    }

    @Override
    public Observable<BaseResp<ArticleCollectBean>> addCollectArticle(long articleId) {
        return mHttpApi.addCollectArticle(articleId);
    }

    @Override
    public Observable<BaseResp<OutsideArticleCollectBean>> addCollectOutsideArticle(String title, String author, String link) {
        return mHttpApi.addCollectOutsideArticle(title, author, link);
    }

    @Override
    public Observable<BaseResp<ArticleCollectBean>> cancelCollectArticle(long articleId) {
        return mHttpApi.cancelCollectArticle(articleId);
    }

    @Override
    public Observable<BaseResp<ArticleCollectBean>> cancelMyCollectArticle(long articleId) {
        return mHttpApi.cancelMyCollectArticle(articleId);
    }

    @Override
    public Observable<BaseResp<List<KnowledgeArchitectureBean>>> getKnowledgeArchitecture() {
        return mHttpApi.getKnowledgeArchitecture();
    }

    @Override
    public Observable<BaseResp<ArchitectureContentBean>> getArchitectureContent(int id, int page) {
        return mHttpApi.getArchitectureContent(page, id);
    }

    @Override
    public Observable<BaseResp<List<NavigationListBean>>> getNavigationList() {
        return mHttpApi.getNavigationList();
    }

    @Override
    public Observable<BaseResp<List<ProjectClassifyBean>>> getProjectClassify() {
        return mHttpApi.getProjectClassify();
    }

    @Override
    public Observable<BaseResp<ProjectContentBean>> getProjectContent(int page, int cid) {
        return mHttpApi.getProjectContent(page, cid);
    }
}
