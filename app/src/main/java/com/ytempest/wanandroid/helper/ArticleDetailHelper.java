package com.ytempest.wanandroid.helper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ytempest.wanandroid.http.bean.ArticleDetailBean;

/**
 * @author heqidu
 * @since 2020/12/21
 */
public class ArticleDetailHelper {
    private static final ArticleDetailHelper INSTANCE = new ArticleDetailHelper();

    public static ArticleDetailHelper getInstance() {
        return INSTANCE;
    }

    private ArticleDetailHelper() {
    }

    private final MutableLiveData<ArticleDetailBean> mArticleDetailUpdateLive = new MutableLiveData<>();

    public LiveData<ArticleDetailBean> getArticleUpdateDetail() {
        return mArticleDetailUpdateLive;
    }

    public void postUpdate(ArticleDetailBean bean) {
        if (bean != null) {
            mArticleDetailUpdateLive.postValue(bean);
        }
    }
}
