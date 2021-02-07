package com.ytempest.wanandroid.utils;

import com.ytempest.wanandroid.http.bean.ArticleCollectBean;
import com.ytempest.wanandroid.http.bean.BaseResp;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heqidu
 * @since 2020/7/4
 */
public class RxUtils {
    /**
     * 切换至子线程处理，并在主线程观察
     */
    public static <T> ObservableTransformer<T, T> switchScheduler() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Function<BaseResp<ArticleCollectBean>, BaseResp<ArticleCollectBean>> checkArticleCollectData() {
        return resp -> {
            // 收藏请求返回的对象为null，则这里处理，防止BaseObserver判断为请求异常
            if (resp.getData() == null) {
                resp.setData(new ArticleCollectBean());
            }
            return resp;
        };
    }
}
