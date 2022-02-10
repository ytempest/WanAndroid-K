package com.ytempest.wanandroid.activity.main.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ytempest.tool.util.LogUtils
import com.ytempest.wanandroid.base.vm.BaseViewModel
import com.ytempest.wanandroid.http.ErrCode
import com.ytempest.wanandroid.http.bean.*
import com.ytempest.wanandroid.utils.PageCtrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author qiduhe
 * @since 2021/8/11
 */
class HomeViewModel(application: Application) : BaseViewModel(application) {

    private val mPageCtrl = PageCtrl()

    val homeDataResult = MutableLiveData<Entity<Pair<List<BannerBean>, HomeArticleBean>>>()
    val homeArticlesResult = MutableLiveData<Entity<Pair<Boolean, HomeArticleBean>>>()
    val homeArticleCollectResult = MutableLiveData<Entity<HomeArticleBean.Data>>()

    fun isUserLogin(): Boolean {
        return mInteractor.configs.getUser().isUserLogin()
    }

    fun <First, Second> requestMulti(
        firstCall: Call<BaseResp<First>>,
        secondCall: Call<BaseResp<Second>>,
        onSuccess: ((First, Second) -> Unit)? = null,
        onFail: ((Int, Throwable?) -> Unit)? = null
    ) {
        viewModelScope.launch {
            val firstTask = async(Dispatchers.IO) { firstCall.execute().body() }
            val secondTask = async(Dispatchers.IO) { secondCall.execute().body() }
            try {
                val firstResp = firstTask.await()
                val secondResp = secondTask.await()

                launch(Dispatchers.Main) {
                    if (firstResp == null || secondResp == null) {
                        onFail?.invoke(ErrCode.EMPTY_RESP, null)
                        return@launch
                    }

                    val firstData = firstResp.getData()
                    val secondData = secondResp.getData()
                    if (firstData == null || secondData == null) {
                        onFail?.invoke(ErrCode.DATA_ERR, null)
                        return@launch
                    }

                    onSuccess?.invoke(firstData, secondData)
                }
            } catch (e: Exception) {
                if (LogUtils.isLoggable()) e.printStackTrace()
                launch(Dispatchers.Main) {
                    when (e) {
                        is HttpException, is UnknownHostException -> onFail?.invoke(
                            ErrCode.NET_ERR,
                            e
                        )
                        is SocketTimeoutException -> onFail?.invoke(ErrCode.REQUEST_ERR, e)
                        else -> onFail?.invoke(ErrCode.UNKNOWN_ERR, e)
                    }
                }
            }
        }
    }

    fun loadHomeData() {
        if (mPageCtrl.isRequesting) return
        mPageCtrl.moveTo(PageCtrl.State.REFRESH)
        requestMulti(
            mInteractor.httpHelper.getBannerList(),
            mInteractor.httpHelper.getHomeArticleList(mPageCtrl.nextPage),
            onSuccess = { bannerList, articleBean ->
                mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                homeDataResult.value = PositiveEntity(Pair(bannerList, articleBean))
            },
            onFail = { code, throwable ->
                mPageCtrl.moveTo(PageCtrl.State.FAIL)
                homeDataResult.value = NegativeEntity(code, throwable)
            }
        )
    }

    fun refreshHomeArticle() {
        mPageCtrl.moveTo(PageCtrl.State.REFRESH)
        val lastVersion: Int = mPageCtrl.version
        request(mInteractor.httpHelper.getHomeArticleList(mPageCtrl.nextPage),
            onSuccess = { homeArticleBean ->
                mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                if (mPageCtrl.isDataVersionValid(lastVersion)) {
                    homeArticlesResult.value = PositiveEntity(Pair(true, homeArticleBean))
                }
            },
            onFail = { code: Int, throwable: Throwable? ->
                mPageCtrl.moveTo(PageCtrl.State.FAIL)
            })
    }

    fun loadMoreHomeArticle() {
        if (mPageCtrl.isRequesting) return
        mPageCtrl.moveTo(PageCtrl.State.LOAD_MORE)
        request(mInteractor.httpHelper.getHomeArticleList(mPageCtrl.nextPage),
            onSuccess = { homeArticleBean ->
                mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                homeArticlesResult.value = PositiveEntity(Pair(false, homeArticleBean))
            },
            onFail = { code: Int, throwable: Throwable? ->
                mPageCtrl.moveTo(PageCtrl.State.FAIL)
            }
        )
    }

    fun updateArticleCollectStatus(article: HomeArticleBean.Data) {
        val isCollect = !article.collect
        val collectCall =
            if (isCollect) mInteractor.httpHelper.addCollectArticle(article.id) // 收藏文章
            else mInteractor.httpHelper.cancelCollectArticle(article.id) // 取消收藏

        request(
            collectCall,
            responseHook = { resp ->
                if (resp.isSuccess()) {
                    // 保证数据不为空
                    resp.setData(resp.getData() ?: ArticleCollectBean())
                }
            },
            onSuccess = { data ->
                article.collect = isCollect
                homeArticleCollectResult.value = PositiveEntity(article)
            },
            onFail = { code, throwable ->
                homeArticleCollectResult.value = NegativeEntity(code, throwable, article)
            }
        )
    }

}