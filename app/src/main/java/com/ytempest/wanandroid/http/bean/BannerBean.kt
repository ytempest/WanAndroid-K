package com.ytempest.wanandroid.http.bean

/**
 * @author heqidu
 * @since 21-2-25
 */

/**
 * desc : 享学~
 * id : 29
 * imagePath : https://www.wanandroid.com/blogimgs/a1b1c600-5570-4eae-8ca0-bea3f4fcafaf.png
 * isVisible : 1
 * order : 0
 * title : 一个Android程序员的自我修养
 * type : 0
 * url : https://www.bilibili.com/video/BV14a411c7mg
 */
data class BannerBean(
        var desc: String = "",
        var id: Int,
        var imagePath: String = "",
        var isVisible: Int,
        var order: Int,
        var title: String = "",
        var type: Int,
        var url: String = "",
)
