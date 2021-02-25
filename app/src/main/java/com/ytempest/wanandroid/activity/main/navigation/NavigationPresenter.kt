package com.ytempest.wanandroid.activity.main.navigation

import com.ytempest.wanandroid.utils.RxUtils
import com.ytempest.wanandroid.base.presenter.BasePresenter
import com.ytempest.wanandroid.http.observer.HandlerObserver
import com.ytempest.wanandroid.interactor.impl.BaseInteractor
import com.ytempest.wanandroid.http.bean.NavigationListBean
import com.ytempest.wanandroid.http.bean.OutsideArticleCollectBean

import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-9
 */
class NavigationPresenter @Inject constructor(
        interactor: BaseInteractor
) : BasePresenter<INavigationView>(interactor), INavigationPresenter {

    override fun getNavigationList() {
        mInteractor.getHttpHelper().getNavigationList()
                .compose(RxUtils.switchScheduler())
                .subscribe(object : HandlerObserver<List<NavigationListBean>>(mView) {
                    override fun onSuccess(list: List<NavigationListBean>) {
                        mView.displayNavigationList(list)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mView.onNavigationListFail(code)
                    }
                })
    }

    override fun addCollectOutsideArticle(article: NavigationListBean.Articles) {
        mInteractor.getHttpHelper().addCollectOutsideArticle(article.title, article.author, article.link)
                .compose(RxUtils.switchScheduler())
                .subscribe(object : HandlerObserver<OutsideArticleCollectBean>(mView, Flags.SHOW_LOADING) {
                    override fun onSuccess(outsideArticleCollectBean: OutsideArticleCollectBean) {
                        mView.onNavigationArticleCollectSuccess(article)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        // 在这个接口服务器返回-1表示已经收藏过了
                        val onceCollected = code == -1
                        mView.onNavigationArticleCollectFail(code, onceCollected, article)
                    }
                })
    }

}