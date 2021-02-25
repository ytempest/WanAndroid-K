package com.ytempest.wanandroid.activity.main.home

import com.ytempest.wanandroid.base.presenter.IPresenter
import com.ytempest.wanandroid.base.view.IView
import com.ytempest.wanandroid.http.bean.BannerBean
import com.ytempest.wanandroid.http.bean.HomeArticleBean
import com.ytempest.wanandroid.http.bean.HomeArticleBean.Data

/**
 * @author heqidu
 * @since 21-2-8
 */
interface IHomeView : IView {
    fun displayHomeArticle(fromRefresh: Boolean, homeArticleBean: HomeArticleBean)

    fun onArticleCollectSuccess(data: HomeArticleBean.Data)

    fun onArticleCollectFail(data: HomeArticleBean.Data, code: Int)

    fun onHomeDataSuccess(bannerList: List<BannerBean>, bean: HomeArticleBean)

    fun onHomeDataFail(code: Int)
}

interface IHomePresenter : IPresenter {
    fun isUserLogin(): Boolean

    fun loadHomeData()

    fun refreshHomeArticle()

    fun loadMoreHomeArticle()

    fun updateArticleCollectStatus(data: HomeArticleBean.Data)
}