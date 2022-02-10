package com.ytempest.wanandroid.activity.article

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ytempest.wanandroid.base.vm.BaseViewModel
import com.ytempest.wanandroid.http.bean.ArticleCollectBean
import com.ytempest.wanandroid.http.bean.Entity
import com.ytempest.wanandroid.http.bean.NegativeEntity
import com.ytempest.wanandroid.http.bean.PositiveEntity

/**
 * @author heqidu
 * @since 21-2-22
 */
class ArticleDetailViewModel(application: Application) : BaseViewModel(application) {

    val articleCollectResult = MutableLiveData<Entity<ArticleCollect>>()

    fun isUserLogin(): Boolean = mInteractor.configs.getUser().isUserLogin()

    fun updateArticleCollectStatus(isCollect: Boolean, articleId: Long) {
        val collectObservable =
            if (isCollect) mInteractor.httpHelper.addCollectArticle(articleId) // 收藏文章
            else mInteractor.httpHelper.cancelCollectArticle(articleId) // 取消收藏

        request(collectObservable,
            responseHook = { resp ->
                if (resp.isSuccess()) {
                    // 保证数据不为空
                    resp.setData(resp.getData() ?: ArticleCollectBean())
                }
            },
            onSuccess = { bean ->
                articleCollectResult.value = PositiveEntity(ArticleCollect(isCollect, articleId))
            },
            onFail = { code, throwable ->
                articleCollectResult.value =
                    NegativeEntity(code, throwable, ArticleCollect(isCollect, articleId))
            }
        )
    }
}