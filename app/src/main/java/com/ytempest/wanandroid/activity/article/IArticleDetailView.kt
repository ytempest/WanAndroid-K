package com.ytempest.wanandroid.activity.article

import com.ytempest.framework.base.view.IView

/**
 * @author heqidu
 * @since 21-2-22
 */

interface IArticleDetailView : IView {
    fun onArticleCollectSuccess(collect: ArticleCollect)

    fun onArticleCollectFail(errCode: Int, collect: ArticleCollect)
}