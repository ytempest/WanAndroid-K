package com.ytempest.wanandroid.http.bean

import androidx.annotation.IntDef
import com.ytempest.wanandroid.http.bean.NavigationListBean.Articles
import com.ytempest.framework.utils.JSON

/**
 * @author heqidu
 * @since 21-2-25
 */

data class ArticleDetailBean(
        @Source
        val source: Int,
        val articleId: Long,
        val author: String,
        val title: String,
        val url: String,
        var isCollected: Boolean = false
) {

    fun isShowCollect(): Boolean {
        return source == Source.HOME || source == Source.KNOWLEDGE
    }

    fun toJson(): String {
        return JSON.toJson(this)
    }


    companion object {
        fun from(json: String?): ArticleDetailBean? {
            return JSON.from(json, ArticleDetailBean::class.java)
        }

        fun from(bean: HomeArticleBean.Data): ArticleDetailBean {
            return ArticleDetailBean(Source.HOME, bean.id, bean.author, bean.title, bean.link, bean.collect)
        }

        fun from(bean: ArchitectureContentBean.Data): ArticleDetailBean {
            return ArticleDetailBean(Source.KNOWLEDGE, bean.id, bean.author, bean.title, bean.link, bean.collect)
        }

        fun from(bean: ProjectContentBean.Data): ArticleDetailBean {
            return ArticleDetailBean(Source.PROJECT, bean.id, bean.author, bean.title, bean.link, bean.collect)
        }

        fun from(bean: Articles): ArticleDetailBean {
            return ArticleDetailBean(Source.NAVIGATION, bean.id, bean.author, bean.title, bean.link, bean.collect)
        }
    }


    @IntDef(Source.HOME, Source.KNOWLEDGE, Source.PROJECT, Source.NAVIGATION)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Source {
        companion object {
            const val HOME = 1
            const val KNOWLEDGE = 2
            const val PROJECT = 3
            const val NAVIGATION = 4
        }
    }
}

