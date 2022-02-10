package com.ytempest.wanandroid.http

import com.ytempest.wanandroid.http.bean.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

/**
 * @author heqidu
 * @since 21-2-7
 */
interface HttpApi {

    /*首页*/

    /**
     * 首页文章列表，一般每页20篇
     *
     * @param pageNum 页码
     * @return 首页文章列表
     */
    @GET("article/list/{pageNum}/json")
    fun getHomeArticleList(@Path("pageNum") pageNum: Int): Call<BaseResp<HomeArticleBean>>

    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username") account: String?,
              @Field("password") password: String?): Call<BaseResp<LoginBean>>

    /**
     * 用户注册
     */
    @FormUrlEncoded
    @POST("user/register")
    fun register(@Field("username") account: String?,
                 @Field("password") pwd: String?,
                 @Field("repassword") confirmPwd: String?): Call<BaseResp<LoginBean>>

    /**
     * 首页Banner
     */
    @GET("banner/json")
    fun getBannerList(): Call<BaseResp<List<BannerBean>>>


    /**
     * 我的收藏列表
     */
    @GET("lg/collect/list/0/json")
    fun getMyCollectionList(): Observable<BaseResp<MyCollectionBean>>

    /**
     * 收藏站内文章
     */
    @POST("lg/collect/{id}/json")
    fun addCollectArticle(@Path("id") articleId: Long): Call<BaseResp<ArticleCollectBean>>

    /**
     * 收藏站外文章
     */
    @FormUrlEncoded
    @POST("lg/collect/add/json")
    fun addCollectOutsideArticle(@Field("title") title: String?,
                                 @Field("author") author: String?,
                                 @Field("link") link: String?)
            : Observable<BaseResp<OutsideArticleCollectBean>>

    /**
     * 取消文章列表的收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun cancelCollectArticle(@Path("id") articleId: Long): Call<BaseResp<ArticleCollectBean>>

    /**
     * 取消我的收藏页面的收藏文章
     */
    @POST("lg/uncollect/{id}/json")
    fun cancelMyCollectArticle(@Path("id") articleId: Long): Observable<BaseResp<ArticleCollectBean>>

    /*知识*/

    /**
     * 获取知识体系大纲
     */
    @GET("tree/json")
    fun getKnowledgeArchitecture(): Call<BaseResp<List<KnowledgeArchitectureBean>>>

    /**
     * 获取指定知识体系下的内容
     */
    @GET("article/list/{page}/json")
    fun getArchitectureContent(@Path("page") page: Int,
                               @Query("cid") id: Int)
            : Call<BaseResp<ArchitectureContentBean>>

    /*导航*/

    @GET("navi/json")
    fun getNavigationList(): Observable<BaseResp<List<NavigationListBean>>>

    /*项目*/

    @GET("project/tree/json")
    fun getProjectClassify(): Observable<BaseResp<List<ProjectClassifyBean>>>

    @GET("project/list/{page}/json")
    fun getProjectContent(@Path("page") page: Int,
                          @Query("cid") cid: Int)
            : Observable<BaseResp<ProjectContentBean>>

}