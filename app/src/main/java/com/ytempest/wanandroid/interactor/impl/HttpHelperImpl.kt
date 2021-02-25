package com.ytempest.wanandroid.interactor.impl

import com.ytempest.wanandroid.http.HttpApi
import com.ytempest.wanandroid.http.bean.BaseResp
import com.ytempest.wanandroid.interactor.HttpHelper
import com.ytempest.wanandroid.http.bean.*
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-8
 */
class HttpHelperImpl @Inject constructor(
        private val mHttpApi: HttpApi
) : HttpHelper {

    override fun getHomeArticleList(pageNum: Int): Observable<BaseResp<HomeArticleBean>> =
            mHttpApi.getHomeArticleList(pageNum)

    override fun login(account: String, password: String): Observable<BaseResp<LoginBean>> =
            mHttpApi.login(account, password)


    override fun register(account: String?, pwd: String?, confirmPwd: String?): Observable<BaseResp<LoginBean>> =
            mHttpApi.register(account, pwd, confirmPwd)

    override fun getBannerList(): Observable<BaseResp<List<BannerBean>>> =
            mHttpApi.getBannerList()

    override fun getMyCollectionList(): Observable<BaseResp<MyCollectionBean>> =
            mHttpApi.getMyCollectionList();

    override fun addCollectArticle(articleId: Long): Observable<BaseResp<ArticleCollectBean>> =
            mHttpApi.addCollectArticle(articleId);


    override fun addCollectOutsideArticle(title: String?, author: String?, link: String?): Observable<BaseResp<OutsideArticleCollectBean>> {
        return mHttpApi.addCollectOutsideArticle(title, author, link)
    }

    override fun cancelCollectArticle(articleId: Long): Observable<BaseResp<ArticleCollectBean>> =
            mHttpApi.cancelCollectArticle(articleId)

    override fun cancelMyCollectArticle(articleId: Long): Observable<BaseResp<ArticleCollectBean>> =
            mHttpApi.cancelMyCollectArticle(articleId)

    override fun getKnowledgeArchitecture(): Observable<BaseResp<List<KnowledgeArchitectureBean>>> =
            mHttpApi.getKnowledgeArchitecture()

    override fun getArchitectureContent(id: Int, page: Int): Observable<BaseResp<ArchitectureContentBean>> {
        return mHttpApi.getArchitectureContent(page, id)
    }

    override fun getNavigationList(): Observable<BaseResp<List<NavigationListBean>>> =
            mHttpApi.getNavigationList()

    override fun getProjectClassify(): Observable<BaseResp<List<ProjectClassifyBean>>> =
            mHttpApi.getProjectClassify()

    override fun getProjectContent(page: Int, cid: Int): Observable<BaseResp<ProjectContentBean>> =
            mHttpApi.getProjectContent(page, cid)
}