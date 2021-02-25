package com.ytempest.wanandroid.activity.main

import androidx.fragment.app.Fragment
import com.ytempest.wanandroid.activity.main.home.HomeFrag
import com.ytempest.wanandroid.activity.main.knowledge.KnowledgeFrag
import com.ytempest.wanandroid.activity.main.navigation.NavigationFrag
import com.ytempest.wanandroid.activity.main.project.ProjectFrag
import com.ytempest.tool.helper.ExpandFragHelper

/**
 * @author heqidu
 * @since 21-2-8
 */
class MainFragConstructor : ExpandFragHelper.FragConstructor {

    override fun createFrag(@MainTab.Id fragId: Int): Fragment {
        return when (fragId) {
            MainTab.Id.HOME -> HomeFrag()
            MainTab.Id.KNOWLEDGE -> KnowledgeFrag()
            MainTab.Id.NAVIGATION -> NavigationFrag();
            MainTab.Id.PROJECT -> ProjectFrag();
            else -> throw IllegalStateException("Not found the fragment for id : $fragId")
        }
    }
}