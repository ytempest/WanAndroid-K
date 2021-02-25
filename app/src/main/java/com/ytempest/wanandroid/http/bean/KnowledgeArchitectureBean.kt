package com.ytempest.wanandroid.http.bean

/**
 * @author heqidu
 * @since 21-2-25
 */
data class KnowledgeArchitectureBean(
        var courseId: Int = 0,
        var id: Int = 0,
        var name: String = "",
        var order: Int = 0,
        var parentChapterId: Int = 0,
        var userControlSetTop: Boolean = false,
        var visible: Int = 0,
        var children: List<Children>? = null,
) {
    data class Children(
            var courseId: Int = 0,
            var id: Int = 0,
            var name: String = "",
            var order: Int = 0,
            var parentChapterId: Int = 0,
            var userControlSetTop: Boolean = false,
            var visible: Int = 0,
            var children: List<*>? = null
    )
}