package com.ytempest.wanandroid.http.bean

/**
 * @author heqidu
 * @since 21-2-25
 */
data class LoginBean(
        var icon: String = "",
        var password: String = "",
        var type: Int = 0,
        var username: String = "",
        var collectIds: List<*>? = null,
)