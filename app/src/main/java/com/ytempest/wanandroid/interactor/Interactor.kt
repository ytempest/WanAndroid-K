package com.ytempest.wanandroid.interactor

import com.ytempest.wanandroid.http.bean.*
import com.ytempest.wanandroid.interactor.configs.UserConfig
import io.reactivex.Observable
import retrofit2.Call

/**
 * @author heqidu
 * @since 21-2-8
 */
interface MvpInteractor {
    fun getHttpHelper(): HttpHelper
    fun getDbHelper(): DbHelper
    fun getConfigs(): Configs
}

interface HttpHelper {
    fun getHomeArticleList(pageNum: Int): Call<BaseResp<HomeArticleBean>>
    fun login(account: String, password: String): Call<BaseResp<LoginBean>>
    fun register(account: String?, pwd: String?, confirmPwd: String?): Call<BaseResp<LoginBean>>
    fun getBannerList(): Call<BaseResp<List<BannerBean>>>
    fun getMyCollectionList(): Observable<BaseResp<MyCollectionBean>>

    fun addCollectArticle(articleId: Long): Call<BaseResp<ArticleCollectBean>>
    fun addCollectOutsideArticle(title: String?, author: String?, link: String?): Observable<BaseResp<OutsideArticleCollectBean>>
    fun cancelCollectArticle(articleId: Long): Call<BaseResp<ArticleCollectBean>>
    fun cancelMyCollectArticle(articleId: Long): Observable<BaseResp<ArticleCollectBean>>
    fun getKnowledgeArchitecture(): Observable<BaseResp<List<KnowledgeArchitectureBean>>>

    fun getArchitectureContent(id: Int, page: Int): Call<BaseResp<ArchitectureContentBean>>
    fun getNavigationList(): Observable<BaseResp<List<NavigationListBean>>>
    fun getProjectClassify(): Observable<BaseResp<List<ProjectClassifyBean>>>

    fun getProjectContent(page: Int, cid: Int): Observable<BaseResp<ProjectContentBean>>
}


interface DbHelper

interface Configs {
    fun getUser(): UserConfig
}

