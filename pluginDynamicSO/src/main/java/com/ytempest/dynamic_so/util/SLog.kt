package com.ytempest.dynamic_so.util

/**
 * @author heqidu
 * @since 2023/9/9
 */
object SLog {

    const val uploadDebug = true

    @JvmStatic
    fun d(tag: String, msg: String) {
        println("[DynamicSOPlugin] $tag, $msg")
    }
}