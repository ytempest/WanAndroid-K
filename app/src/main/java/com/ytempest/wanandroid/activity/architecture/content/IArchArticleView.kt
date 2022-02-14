package com.ytempest.wanandroid.activity.architecture.content

import com.ytempest.framework.base.view.IView
import com.ytempest.wanandroid.http.bean.ArchitectureContentBean

/**
 * @author heqidu
 * @since 21-2-22
 */

interface IArchArticleView : IView {
    fun onLoadArchitectureContent(architectureContentBean: ArchitectureContentBean, fromRefresh: Boolean)

    fun onArchArticleCollectSuccess(article: ArchitectureContentBean.Data)

    fun onArchArticleCollectFail(article: ArchitectureContentBean.Data, code: Int)

    fun onRefreshArchitectureFail(code: Int)
}