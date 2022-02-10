package com.ytempest.wanandroid.activity.main.project.content

import com.ytempest.wanandroid.base.presenter.BasePresenter
import com.ytempest.wanandroid.http.bean.BaseResp
import com.ytempest.wanandroid.http.bean.OutsideArticleCollectBean
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean
import com.ytempest.wanandroid.http.bean.ProjectContentBean
import com.ytempest.wanandroid.http.observer.HandlerObserver
import com.ytempest.wanandroid.interactor.impl.BaseInteractor
import com.ytempest.wanandroid.utils.PageCtrl
import com.ytempest.wanandroid.utils.RxUtils
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-10
 */
class ProjectContentPresenter @Inject constructor(
        interactor: BaseInteractor
) : BasePresenter<IProjectContentView>(interactor), IProjectContentPresenter {

    private val mPageCtrl = PageCtrl()

    override fun refreshContent(classify: ProjectClassifyBean) {
        mPageCtrl.moveTo(PageCtrl.State.REFRESH)
        mInteractor.getHttpHelper().getProjectContent(mPageCtrl.nextPage, classify.id)
                .compose(RxUtils.switchScheduler())
//                .filter(mPageCtrl.filterDirtyData())
                .subscribe(object : HandlerObserver<ProjectContentBean>(mView) {
                    override fun onSuccess(projectContent: ProjectContentBean) {
                        mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                        mView.displayProjectContent(projectContent)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mView.onProjectContentFail(code)
                        mPageCtrl.moveTo(PageCtrl.State.FAIL)
                    }
                })
    }

    override fun loadMoreProjectContent(classifyBean: ProjectClassifyBean) {
        if (mPageCtrl.isRequesting) return
        mPageCtrl.moveTo(PageCtrl.State.LOAD_MORE)
        mInteractor.getHttpHelper().getProjectContent(mPageCtrl.nextPage, classifyBean.id)
                .compose<BaseResp<ProjectContentBean>>(RxUtils.switchScheduler<BaseResp<ProjectContentBean>>())
//                .filter(mPageCtrl.filterDirtyData())
                .subscribe(object : HandlerObserver<ProjectContentBean>(mView, Flags.SHOW_ERR_MSG) {
                    override fun onSuccess(projectContent: ProjectContentBean) {
                        mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                        mView.onMoreProjectContentLoaded(projectContent)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mPageCtrl.moveTo(PageCtrl.State.FAIL)
                    }
                })
    }

    override fun addProjectArticleCollect(bean: ProjectContentBean.Data) {
        mInteractor.getHttpHelper().addCollectOutsideArticle(bean.title, bean.author, bean.link)
                .compose(RxUtils.switchScheduler())
                .subscribe(object : HandlerObserver<OutsideArticleCollectBean>(mView, Flags.SHOW_LOADING) {
                    override fun onSuccess(data: OutsideArticleCollectBean) {
                        bean.collect = true
                        mView.onProjectArticleCollectSuccess(bean)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        // 在这个接口服务器返回-1表示已经收藏过了
                        val onceCollected = code == -1
                        mView.onProjectArticleCollectFail(code, onceCollected, bean)
                    }
                })
    }
}