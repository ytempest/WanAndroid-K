package com.ytempest.wanandroid.activity.main.home

import com.ytempest.wanandroid.http.bean.BaseResp
import com.ytempest.tool.util.DataUtils
import com.ytempest.wanandroid.base.presenter.BasePresenter
import com.ytempest.wanandroid.http.bean.ArticleCollectBean
import com.ytempest.wanandroid.http.bean.BannerBean
import com.ytempest.wanandroid.http.bean.HomeArticleBean
import com.ytempest.wanandroid.http.observer.HandlerObserver
import com.ytempest.wanandroid.interactor.impl.BaseInteractor
import com.ytempest.wanandroid.utils.PageCtrl
import com.ytempest.wanandroid.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.Observer
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-8
 */
class HomePresenter @Inject constructor(
        interactor: BaseInteractor
) : BasePresenter<IHomeView>(interactor), IHomePresenter {

    private val mPageCtrl: PageCtrl = PageCtrl()

    override fun isUserLogin(): Boolean {
        return mInteractor.getConfigs().getUser().isUserLogin()
    }

    override fun loadHomeData() {
        if (mPageCtrl.isRequesting) return
        mPageCtrl.moveTo(PageCtrl.State.REFRESH)
        Observable.merge(
                mInteractor.getHttpHelper().getBannerList(),
                mInteractor.getHttpHelper().getHomeArticleList(mPageCtrl.nextPage)
        ).compose(RxUtils.switchScheduler())
                .subscribe(object : HandlerObserver<Any>(mView) {
                    private var mBannerList: List<BannerBean>? = null
                    private var mArticleBean: HomeArticleBean? = null

                    override fun onSuccess(data: Any) {
                        if (data is List<*>) {
                            mBannerList = data as List<BannerBean>
                        } else if (data is HomeArticleBean) {
                            mArticleBean = data
                        }
                        if (!DataUtils.isNull(mBannerList, mArticleBean)) {
                            mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                            mView.onHomeDataSuccess(mBannerList!!, mArticleBean!!)
                        }
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        mPageCtrl.moveTo(PageCtrl.State.FAIL)
                        mView.onHomeDataFail(code)
                    }
                } as Observer<in BaseResp<out Any>>)
    }

    override fun refreshHomeArticle() {
        mPageCtrl.moveTo(PageCtrl.State.REFRESH)
        mInteractor.getHttpHelper().getHomeArticleList(mPageCtrl.nextPage)
                .compose(RxUtils.switchScheduler())
                .filter(mPageCtrl.filterDirtyData())
                .subscribe(object : HandlerObserver<HomeArticleBean>(mView) {
                    override fun onSuccess(bean: HomeArticleBean) {
                        mView.displayHomeArticle(true, bean)
                        mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mPageCtrl.moveTo(PageCtrl.State.FAIL)
                    }
                })
    }

    override fun loadMoreHomeArticle() {
        if (mPageCtrl.isRequesting) return
        mPageCtrl.moveTo(PageCtrl.State.LOAD_MORE)
        mInteractor.getHttpHelper().getHomeArticleList(mPageCtrl.nextPage)
                .compose(RxUtils.switchScheduler())
                .filter(mPageCtrl.filterDirtyData())
                .subscribe(object : HandlerObserver<HomeArticleBean>(mView, Flags.SHOW_ERR_MSG) {

                    override fun onSuccess(bean: HomeArticleBean) {
                        mView.displayHomeArticle(false, bean)
                        mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        super.onFail(code, e)
                        mPageCtrl.moveTo(PageCtrl.State.FAIL)
                    }
                })
    }

    override fun updateArticleCollectStatus(data: HomeArticleBean.Data) {
        val isCollect = !data.collect
        val collectObservable = if (isCollect) mInteractor.getHttpHelper().addCollectArticle(data.id) // 收藏文章
        else mInteractor.getHttpHelper().cancelCollectArticle(data.id) // 取消收藏

        collectObservable.compose(RxUtils.switchScheduler())
                .map(RxUtils.checkArticleCollectData())
                .subscribe(object : HandlerObserver<ArticleCollectBean>(mView, Flags.SHOW_LOADING) {
                    override fun onSuccess(bean: ArticleCollectBean) {
                        data.collect = isCollect
                        mView.onArticleCollectSuccess(data)
                    }

                    override fun onFail(code: Int, e: Throwable) {
                        data.collect = isCollect
                        mView.onArticleCollectFail(data, code)
                    }
                })
    }
}
