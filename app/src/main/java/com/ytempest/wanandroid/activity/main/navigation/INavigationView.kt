package com.ytempest.wanandroid.activity.main.navigation


import com.ytempest.wanandroid.base.presenter.IPresenter
import com.ytempest.wanandroid.base.view.IView
import com.ytempest.wanandroid.http.bean.NavigationListBean


/**
 * @author heqidu
 * @since 21-2-9
 */
interface INavigationView : IView {
    fun displayNavigationList(list: List<NavigationListBean>)
    fun onNavigationListFail(code: Int)
    fun onNavigationArticleCollectSuccess(article: NavigationListBean.Articles)
    fun onNavigationArticleCollectFail(code: Int, onceCollected: Boolean)
}