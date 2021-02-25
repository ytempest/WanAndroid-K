package com.ytempest.wanandroid.http.bean

/**
 * @author heqidu
 * @since 21-2-25
 */
class NavigationListBean(
        var cid: Int = 0,
        var name: String = "",
        var articles: List<Articles>? = null
) {
    data class Articles(
            var apkLink: String = "",
            var audit: Int = 0,
            var author: String = "",
            var canEdit: Boolean = false,
            var chapterId: Int = 0,
            var chapterName: String = "",
            var collect: Boolean = false,
            var courseId: Int = 0,
            var desc: String = "",
            var descMd: String = "",
            var envelopePic: String = "",
            var fresh: Boolean = false,
            var host: String = "",
            var id: Long = 0L,
            var link: String = "",
            var niceDate: String = "",
            var niceShareDate: String = "",
            var origin: String = "",
            var prefix: String = "",
            var projectLink: String = "",
            var publishTime: Long = 0L,
            var realSuperChapterId: Int = 0,
            var selfVisible: Int = 0,
            var shareDate: String = "",
            var shareUser: String = "",
            var superChapterId: Int = 0,
            var superChapterName: String = "",
            var title: String = "",
            var type: Int = 0,
            var userId: Int = 0,
            var visible: Int = 0,
            var zan: Int = 0,
            var tags: List<*>? = null,
    )
}