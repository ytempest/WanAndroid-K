package com.ytempest.wanandroid.http.bean

/**
 * @author heqidu
 * @since 21-2-25
 */
class ProjectContentBean(
        var curPage: Int = 0,
        var offset: Int = 0,
        var over: Boolean = false,
        var pageCount: Int = 0,
        var size: Int = 0,
        var total: Int = 0,
        var datas: List<Data>? = null,
) {
    data class Data(
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
            var shareDate: Long = 0L,
            var shareUser: String = "",
            var superChapterId: Int = 0,
            var superChapterName: String = "",
            var title: String = "",
            var type: Int = 0,
            var userId: Long = 0L,
            var visible: Int = 0,
            var zan: Int = 0,
            var tags: List<Tags>? = null,
    ) {
        data class Tags(
                var name: String = "",
                var url: String = ""
        )

    }
}