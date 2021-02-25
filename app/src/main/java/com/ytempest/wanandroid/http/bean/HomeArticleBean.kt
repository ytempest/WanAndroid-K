package com.ytempest.wanandroid.http.bean

/**
 * @author heqidu
 * @since 21-2-25
 */
data class HomeArticleBean(
        var curPage: Int,
        var offset: Int,
        var over: Boolean = false,
        var pageCount: Int,
        var size: Int,
        var total: Int,
        var datas: List<Data>? = null
) {
    data class Data(
            var apkLink: String = "",
            var audit: Int,
            var author: String = "",
            var canEdit: Boolean,
            var chapterId: Int,
            var chapterName: String = "",
            var collect: Boolean,
            var courseId: Int,
            var desc: String = "",
            var descMd: String = "",
            var envelopePic: String = "",
            var fresh: Boolean,
            var id: Long,
            var link: String = "",
            var niceDate: String = "",
            var niceShareDate: String = "",
            var origin: String = "",
            var prefix: String = "",
            var projectLink: String = "",
            var publishTime: Long,
            var realSuperChapterId: Int,
            var selfVisible: Int,
            var shareDate: Long,
            var shareUser: String = "",
            var superChapterId: Int,
            var superChapterName: String = "",
            var title: String = "",
            var type: Int,
            var userId: Int,
            var visible: Int,
            var zan: Int,
            var tags: List<*>? = null
    )
}