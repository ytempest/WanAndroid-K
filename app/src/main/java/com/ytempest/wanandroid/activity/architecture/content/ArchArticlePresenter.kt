package com.ytempest.wanandroid.activity.architecture.content

import com.ytempest.wanandroid.base.presenter.BasePresenter
import com.ytempest.wanandroid.http.observer.HandlerObserver
import com.ytempest.wanandroid.interactor.impl.BaseInteractor
import com.ytempest.wanandroid.utils.RxUtils
import com.ytempest.wanandroid.http.bean.ArchitectureContentBean
import com.ytempest.wanandroid.http.bean.ArticleCollectBean
import com.ytempest.wanandroid.utils.PageCtrl
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-22
 */
class ArchArticlePresenter @Inject constructor(
        interactor: BaseInteractor
) : BasePresenter<IArchArticleView>(interactor), IArchArticlePresenter {

    private val mPageCtrl = PageCtrl()

    override fun isUserLogin(): Boolean {
        return mInteractor.getConfigs().getUser().isUserLogin()
    }

    override fun refreshArchitectureContent(id: Int) {
        if (mPageCtrl.isRequesting) return
        mPageCtrl.moveTo(PageCtrl.State.REFRESH)
        mInteractor.getHttpHelper().getArchitectureContent(id, mPageCtrl.nextPage)
                .compose(RxUtils.switchScheduler())
                .subscribe(object : HandlerObserver<ArchitectureContentBean>(mView) {
                    override fun onSuccess(bean: ArchitectureContentBean) {
                        mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                        mView.onLoadArchitectureContent(bean, true)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mPageCtrl.moveTo(PageCtrl.State.FAIL)
                        mView.onRefreshArchitectureFail(code)
                    }
                })
    }

    override fun loadMoreArchitectureContent(id: Int) {
        if (mPageCtrl.isRequesting) return
        mPageCtrl.moveTo(PageCtrl.State.LOAD_MORE)
        mInteractor.getHttpHelper().getArchitectureContent(id, mPageCtrl.nextPage)
                .compose(RxUtils.switchScheduler())
                .subscribe(object : HandlerObserver<ArchitectureContentBean>(mView) {
                    override fun onSuccess(architectureContentBean: ArchitectureContentBean) {
                        mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                        mView.onLoadArchitectureContent(architectureContentBean, false)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mPageCtrl.moveTo(PageCtrl.State.FAIL)
                    }
                })
    }

    override fun updateArticleCollectStatus(isCollect: Boolean, article: ArchitectureContentBean.Data) {
        val collectObservable = if (isCollect) mInteractor.getHttpHelper().addCollectArticle(article.id.toLong()) // 收藏文章
        else mInteractor.getHttpHelper().cancelCollectArticle(article.id.toLong()) // 取消收藏

        collectObservable.compose(RxUtils.switchScheduler())
                .map(RxUtils.checkArticleCollectData())
                .subscribe(object : HandlerObserver<ArticleCollectBean>(mView, Flags.SHOW_LOADING) {
                    override fun onSuccess(bean: ArticleCollectBean) {
                        mView.onArchArticleCollectSuccess(isCollect, article)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mView.onArchArticleCollectFail(isCollect, article, code)
                    }
                })
    }
}