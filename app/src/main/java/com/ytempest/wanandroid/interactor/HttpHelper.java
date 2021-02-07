package com.ytempest.wanandroid.interactor;

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

import java.util.List;

import io.reactivex.Observable;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public interface HttpHelper {

    Observable<BaseResp<HomeArticleBean>> getHomeArticleList(int pageNum);

    Observable<BaseResp<LoginBean>> login(String account, String password);

    Observable<BaseResp<LoginBean>> register(String account, String pwd, String confirmPwd);

    Observable<BaseResp<List<BannerBean>>> getBannerList();

    Observable<BaseResp<MyCollectionBean>> getMyCollectionList();

    Observable<BaseResp<ArticleCollectBean>> addCollectArticle(long articleId);

    Observable<BaseResp<OutsideArticleCollectBean>> addCollectOutsideArticle(String title, String author, String link);

    Observable<BaseResp<ArticleCollectBean>> cancelCollectArticle(long articleId);

    Observable<BaseResp<ArticleCollectBean>> cancelMyCollectArticle(long articleId);

    Observable<BaseResp<List<KnowledgeArchitectureBean>>> getKnowledgeArchitecture();

    Observable<BaseResp<ArchitectureContentBean>> getArchitectureContent(int id, int page);

    Observable<BaseResp<List<NavigationListBean>>> getNavigationList();

    Observable<BaseResp<List<ProjectClassifyBean>>> getProjectClassify();

    Observable<BaseResp<ProjectContentBean>> getProjectContent(int page, int cid);

}
