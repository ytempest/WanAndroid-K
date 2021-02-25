package com.ytempest.wanandroid.activity.architecture.content

import com.ytempest.wanandroid.base.presenter.IPresenter
import com.ytempest.wanandroid.base.view.IView
import com.ytempest.wanandroid.http.bean.ArchitectureContentBean

/**
 * @author heqidu
 * @since 21-2-22
 */

interface IArchArticleView : IView {
    fun onLoadArchitectureContent(architectureContentBean: ArchitectureContentBean, fromRefresh: Boolean)

    fun onArchArticleCollectSuccess(isCollect: Boolean, article: ArchitectureContentBean.Data)

    fun onArchArticleCollectFail(isCollect: Boolean, article: ArchitectureContentBean.Data, code: Int)

    fun onRefreshArchitectureFail(code: Int)
}

interface IArchArticlePresenter : IPresenter {
    fun isUserLogin(): Boolean

    fun refreshArchitectureContent(id: Int)

    fun loadMoreArchitectureContent(id: Int)

    fun updateArticleCollectStatus(isCollect: Boolean, article: ArchitectureContentBean.Data)
}