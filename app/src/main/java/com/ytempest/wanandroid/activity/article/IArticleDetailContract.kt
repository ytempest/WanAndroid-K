package com.ytempest.wanandroid.activity.article

import com.ytempest.wanandroid.base.presenter.IPresenter
import com.ytempest.wanandroid.base.view.IView

/**
 * @author heqidu
 * @since 21-2-22
 */

interface IArticleDetailView : IView {
    fun onArticleCollectSuccess(isCollect: Boolean, articleId: Long)

    fun onArticleCollectFail(isCollect: Boolean, articleId: Long, errCode: Int)
}

interface IArticleDetailPresenter : IPresenter {
    fun isUserLogin(): Boolean

    fun updateArticleCollectStatus(isCollect: Boolean, articleId: Long)
}