package com.ytempest.wanandroid.utils

import com.ytempest.wanandroid.http.bean.BaseResp
import com.ytempest.wanandroid.http.bean.ArticleCollectBean
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * @author heqidu
 * @since 21-2-8
 */
object RxUtils {
    /**
     * 切换至子线程处理，并在主线程观察
     */
    fun <T> switchScheduler(): ObservableTransformer<T, T>? {
        return ObservableTransformer { upstream: Observable<T> ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun checkArticleCollectData(): Function<BaseResp<ArticleCollectBean>, BaseResp<ArticleCollectBean>> {
        return Function { resp: BaseResp<ArticleCollectBean> ->
            // 收藏请求返回的对象为null，则这里处理，防止BaseObserver判断为请求异常
            if (resp.getData() == null) {
                resp.setData(ArticleCollectBean())
            }
            resp
        }
    }
}