package com.ytempest.wanandroid.activity.main.project


import com.ytempest.wanandroid.base.presenter.IPresenter
import com.ytempest.wanandroid.base.view.IView
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean

/**
 * @author heqidu
 * @since 21-2-10
 */
interface IProjectView : IView {
    fun onProjectClassifyReceived(list: List<ProjectClassifyBean>)
    fun onProjectClassifyFail(code: Int)
}

interface IProjectPresenter : IPresenter {
    fun getProjectClassify()
}