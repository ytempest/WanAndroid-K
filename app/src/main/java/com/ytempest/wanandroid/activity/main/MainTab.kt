package com.ytempest.wanandroid.activity.main

import androidx.annotation.IntDef
import com.ytempest.wanandroid.R

/**
 * @author heqidu
 * @since 21-2-8
 */
enum class MainTab(
        @Id val id: Int,
        val normalIcon: Int,
        val selectIcon: Int,
        val title: Int
) {

    // TAB的顺序通过这里的顺序控制
    HOME(Id.HOME, R.mipmap.ic_tab_home, R.mipmap.ic_tab_home_selected, R.string.tab_home),
    KNOWLEDGE(Id.KNOWLEDGE, R.mipmap.ic_tab_knowledge, R.mipmap.ic_tab_knowledge_selected, R.string.tab_knowledge),
    NAVIGATION(Id.NAVIGATION, R.mipmap.ic_tab_navigation, R.mipmap.ic_tab_navigation_selected, R.string.tab_navigation),
    PROJECT(Id.PROJECT, R.mipmap.ic_tab_project, R.mipmap.ic_tab_project_selected, R.string.tab_project),
    ;

    @IntDef(Id.HOME, Id.KNOWLEDGE, Id.NAVIGATION, Id.PROJECT)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Id {
        companion object {
            const val HOME = 0
            const val KNOWLEDGE = 1
            const val NAVIGATION = 2
            const val PROJECT = 3
        }
    }
}