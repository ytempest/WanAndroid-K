package com.ytempest.wanandroid.activity.main.project.content

import com.ytempest.wanandroid.base.presenter.IPresenter
import com.ytempest.wanandroid.base.view.IView
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean
import com.ytempest.wanandroid.http.bean.ProjectContentBean

/**
 * @author heqidu
 * @since 21-2-10
 */
interface IProjectContentView : IView {
    fun displayProjectContent(projectContent: ProjectContentBean)
    fun onProjectContentFail(code: Int)
    fun onMoreProjectContentLoaded(projectContent: ProjectContentBean)
    fun onProjectArticleCollectSuccess(bean: ProjectContentBean.Data)
    fun onProjectArticleCollectFail(code: Int, onceCollected: Boolean, bean: ProjectContentBean.Data)
}

interface IProjectContentPresenter : IPresenter {
    fun refreshContent(classifyBean: ProjectClassifyBean)
    fun loadMoreProjectContent(classifyBean: ProjectClassifyBean)
    fun addProjectArticleCollect(bean: ProjectContentBean.Data)
}
