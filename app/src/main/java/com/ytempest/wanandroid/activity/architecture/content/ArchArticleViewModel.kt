package com.ytempest.wanandroid.activity.architecture.content

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ytempest.wanandroid.base.vm.BaseViewModel
import com.ytempest.wanandroid.http.bean.*

import com.ytempest.wanandroid.utils.PageCtrl
import kotlinx.coroutines.launch

/**
 * @author qiduhe
 * @since 2021/8/10
 */
class ArchArticleViewModel(application: Application) : BaseViewModel(application) {

    private val mPageCtrl = PageCtrl()

    val architectureContentResult = MutableLiveData<Entity<ArchitectureContentBean>>()
    val moreArchitectureContentResult = MutableLiveData<Entity<ArchitectureContentBean>>()
    val archArticleCollectResult = MutableLiveData<Entity<ArchitectureContentBean.Data>>()

    fun isUserLogin(): Boolean {
        return mInteractor.configs.getUser().isUserLogin()
    }

    fun refreshArchitectureContent(id: Int) {
        if (mPageCtrl.isRequesting) return
        mPageCtrl.moveTo(PageCtrl.State.REFRESH)

        request(mInteractor.httpHelper.getArchitectureContent(id, mPageCtrl.nextPage),
            onSuccess = { data ->
                mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                architectureContentResult.value = PositiveEntity(data)
            },
            onFail = { code, throwable ->
                mPageCtrl.moveTo(PageCtrl.State.FAIL)
                architectureContentResult.value = NegativeEntity(code, throwable)
            }
        )
    }

    fun loadMoreArchitectureContent(id: Int) {
        if (mPageCtrl.isRequesting) return
        mPageCtrl.moveTo(PageCtrl.State.LOAD_MORE)

        viewModelScope.launch {
            request(mInteractor.httpHelper.getArchitectureContent(id, mPageCtrl.nextPage),
                onSuccess = { data ->
                    mPageCtrl.moveTo(PageCtrl.State.SUCCESS)
                    moreArchitectureContentResult.value = PositiveEntity(data)
                },
                onFail = { code, throwable ->
                    mPageCtrl.moveTo(PageCtrl.State.FAIL)
                    moreArchitectureContentResult.value = NegativeEntity(code, throwable)
                }
            )
        }
    }

    fun updateArticleCollectStatus(isCollect: Boolean, article: ArchitectureContentBean.Data) {
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
                archArticleCollectResult.value = PositiveEntity(article)
            },
            onFail = { code, throwable ->
                archArticleCollectResult.value = NegativeEntity(code, throwable, article)
            }
        )
    }
}