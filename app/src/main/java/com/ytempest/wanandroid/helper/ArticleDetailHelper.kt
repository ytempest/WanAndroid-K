package com.ytempest.wanandroid.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ytempest.wanandroid.http.bean.ArticleDetailBean

/**
 * @author heqidu
 * @since 21-2-9
 */
class ArticleDetailHelper private constructor() {

    companion object {
        val instance: ArticleDetailHelper by lazy { ArticleDetailHelper() }
    }

    private val mArticleDetailUpdateLive: MutableLiveData<ArticleDetailBean> = MutableLiveData()

    fun getArticleUpdateDetail(): LiveData<ArticleDetailBean> = mArticleDetailUpdateLive

    fun postUpdate(bean: ArticleDetailBean?) {
        bean?.let {
            mArticleDetailUpdateLive.postValue(bean)
        }
    }
}