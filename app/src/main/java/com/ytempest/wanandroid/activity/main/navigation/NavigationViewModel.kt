package com.ytempest.wanandroid.activity.main.navigation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ytempest.wanandroid.base.vm.BaseViewModel
import com.ytempest.wanandroid.http.bean.Entity
import com.ytempest.wanandroid.http.bean.NavigationListBean
import com.ytempest.wanandroid.http.bean.NegativeEntity
import com.ytempest.wanandroid.http.bean.PositiveEntity

/**
 * @author heqidu
 * @since 21-2-9
 */
class NavigationViewModel(application: Application) : BaseViewModel(application) {

    val navigationListResult = MutableLiveData<Entity<List<NavigationListBean>>>()
    val outsideArticleCollectResult = MutableLiveData<Entity<NavigationListBean.Articles>>()

    fun loadNavigationList() {
        request(
            mInteractor.httpHelper.getNavigationList(),
            onSuccess = { list ->
                navigationListResult.value = PositiveEntity(list)
            },
            onFail = { code, throwable ->
                navigationListResult.value = NegativeEntity(code, throwable)
            }
        )
    }

    fun addCollectOutsideArticle(article: NavigationListBean.Articles) {
        request(
            mInteractor.httpHelper.addCollectOutsideArticle(
                article.title,
                article.author,
                article.link
            ),
            onSuccess = { bean ->
                outsideArticleCollectResult.value = PositiveEntity(article)
            },
            onFail = { code, throwable ->
                // 在这个接口服务器返回-1表示已经收藏过了
                val onceCollected = (code == -1)
                outsideArticleCollectResult.value = NegativeEntity(code, throwable, onceCollected)

            }
        )
    }

}