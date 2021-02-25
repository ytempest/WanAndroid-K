package com.ytempest.wanandroid.activity.article

import com.ytempest.wanandroid.base.presenter.BasePresenter
import com.ytempest.wanandroid.http.observer.HandlerObserver
import com.ytempest.wanandroid.interactor.impl.BaseInteractor
import com.ytempest.wanandroid.utils.RxUtils
import com.ytempest.wanandroid.http.bean.ArticleCollectBean
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-22
 */
class ArticleDetailPresenter @Inject constructor(
        interactor: BaseInteractor
) : BasePresenter<IArticleDetailView>(interactor), IArticleDetailPresenter {

    override fun isUserLogin(): Boolean = mInteractor.getConfigs().getUser().isUserLogin()

    override fun updateArticleCollectStatus(isCollect: Boolean, articleId: Long) {
        val collectObservable = if (isCollect) mInteractor.getHttpHelper().addCollectArticle(articleId) // 收藏文章
        else mInteractor.getHttpHelper().cancelCollectArticle(articleId) // 取消收藏

        collectObservable.compose(RxUtils.switchScheduler())
                .map(RxUtils.checkArticleCollectData())
                .subscribe(object : HandlerObserver<ArticleCollectBean>(mView, Flags.SHOW_LOADING) {
                    override fun onSuccess(bean: ArticleCollectBean) {
                        mView.onArticleCollectSuccess(isCollect, articleId)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mView.onArticleCollectFail(isCollect, articleId, code)
                    }
                })
    }
}