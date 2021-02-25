package com.ytempest.wanandroid.http.bean

/**
 * @author heqidu
 * @since 21-2-25
 */
data class MyCollectionBean(
        var curPage: Int = 0,
        var offset: Int = 0,
        var over: Boolean = false,
        var pageCount: Int = 0,
        var size: Int = 0,
        var total: Int = 0,
        var datas: List<Data>? = null,
) {
    data class Data(
            var author: String = "",
            var chapterId: Int = 0,
            var chapterName: String = "",
            var courseId: Int = 0,
            var desc: String = "",
            var envelopePic: String = "",
            var id: Int = 0,
            var link: String = "",
            var niceDate: String = "",
            var origin: String = "",
            var originId: Int = 0,
            var publishTime: Long = 0L,
            var title: String = "",
            var userId: Long = 0,
            var visible: Int = 0,
            var zan: Int = 0
    )
}