package com.ytempest.wanandroid.activity.main.project.content

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ytempest.wanandroid.base.vm.BaseViewModel
import com.ytempest.wanandroid.http.bean.*
import com.ytempest.wanandroid.utils.PageCtrl

/**
 * @author heqidu
 * @since 21-2-10
 */
class ProjectContentViewModel(application: Application) : BaseViewModel(application) {

    private val mPageCtrl = PageCtrl()

    val projectContentResult = MutableLiveData<Entity<ProjectContentBean>>()
    val projectArticleCollectResult = MutableLiveData<Entity<ProjectContentBean.Data>>()

    fun refreshContent(classify: ProjectClassifyBean) {
        mPageCtrl.moveTo(PageCtrl.State.REFRESH)
        val lastVersion: Int = mPageCtrl.version
        request(
            mInteractor.httpHelper.getProjectContent(mPageCtrl.nextPage, classify.id),
            onSuccess = { bean ->
                mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                if (mPageCtrl.isDataVersionValid(lastVersion)) {
                    projectContentResult.value = PositiveEntity(bean)
                }
            },
            onFail = { code: Int, throwable: Throwable? ->
                mPageCtrl.moveTo(PageCtrl.State.FAIL)
                projectContentResult.value = NegativeEntity(code, throwable)
            }
        )
    }

    fun loadMoreProjectContent(classifyBean: ProjectClassifyBean) {
        if (mPageCtrl.isRequesting) return
        mPageCtrl.moveTo(PageCtrl.State.LOAD_MORE)
        val lastVersion: Int = mPageCtrl.version
        request(
            mInteractor.httpHelper.getProjectContent(mPageCtrl.nextPage, classifyBean.id),
            onSuccess = { bean ->
                mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                if (mPageCtrl.isDataVersionValid(lastVersion)) {
                    projectContentResult.value = PositiveEntity(bean, true)
                }
            },
            onFail = { code: Int, throwable: Throwable? ->
                mPageCtrl.moveTo(PageCtrl.State.FAIL)
            }
        )
    }

    fun addProjectArticleCollect(bean: ProjectContentBean.Data) {
        request(
            mInteractor.httpHelper.addCollectOutsideArticle(bean.title, bean.author, bean.link),
            onSuccess = { data ->
                bean.collect = true
                projectArticleCollectResult.value = PositiveEntity(bean)
            },
            onFail = { code: Int, throwable: Throwable? ->
                // 在这个接口服务器返回-1表示已经收藏过了
                val onceCollected = code == -1
                projectArticleCollectResult.value = NegativeEntity(code, throwable, onceCollected)
            }
        )
    }
}