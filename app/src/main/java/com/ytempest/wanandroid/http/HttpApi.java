package com.ytempest.wanandroid.http;

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
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author heqidu
 * @since 2020/7/4
 */
public interface HttpApi {

    String BASE_URL = "https://www.wanandroid.com/";

    /*首页*/

    /**
     * 首页文章列表，一般每页20篇
     *
     * @param pageNum 页码
     * @return 首页文章列表
     */
    @GET("article/list/{pageNum}/json")
    Observable<BaseResp<HomeArticleBean>> getHomeArticleList(@Path("pageNum") int pageNum);

    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST("user/login")
    Observable<BaseResp<LoginBean>> login(@Field("username") String account,
                                          @Field("password") String password);

    /**
     * 用户注册
     */
    @FormUrlEncoded
    @POST("user/register")
    Observable<BaseResp<LoginBean>> register(@Field("username") String account,
                                             @Field("password") String pwd,
                                             @Field("repassword") String confirmPwd);

    /**
     * 首页Banner
     */
    @GET("banner/json")
    Observable<BaseResp<List<BannerBean>>> getBannerList();


    /**
     * 我的收藏列表
     */
    @GET("lg/collect/list/0/json")
    Observable<BaseResp<MyCollectionBean>> getMyCollectionList();

    /**
     * 收藏站内文章
     */
    @POST("lg/collect/{id}/json")
    Observable<BaseResp<ArticleCollectBean>> addCollectArticle(@Path("id") long articleId);

    /**
     * 收藏站外文章
     */
    @FormUrlEncoded
    @POST("lg/collect/add/json")
    Observable<BaseResp<OutsideArticleCollectBean>> addCollectOutsideArticle(@Field("title") String title,
                                                                             @Field("author") String author,
                                                                             @Field("link") String link);

    /**
     * 取消文章列表的收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<BaseResp<ArticleCollectBean>> cancelCollectArticle(@Path("id") long articleId);

    /**
     * 取消我的收藏页面的收藏文章
     */
    @POST("lg/uncollect/{id}/json")
    Observable<BaseResp<ArticleCollectBean>> cancelMyCollectArticle(@Path("id") long articleId);

    /*知识*/

    /**
     * 获取知识体系大纲
     */
    @GET("tree/json")
    Observable<BaseResp<List<KnowledgeArchitectureBean>>> getKnowledgeArchitecture();

    /**
     * 获取指定知识体系下的内容
     */
    @GET("article/list/{page}/json")
    Observable<BaseResp<ArchitectureContentBean>> getArchitectureContent(@Path("page") int page,
                                                                         @Query("cid") int id);

    /*导航*/

    @GET("navi/json")
    Observable<BaseResp<List<NavigationListBean>>> getNavigationList();

    /*项目*/

    @GET("project/tree/json")
    Observable<BaseResp<List<ProjectClassifyBean>>> getProjectClassify();

    @GET("project/list/{page}/json")
    Observable<BaseResp<ProjectContentBean>> getProjectContent(@Path("page") int page,
                                                               @Query("cid") int cid);

}
