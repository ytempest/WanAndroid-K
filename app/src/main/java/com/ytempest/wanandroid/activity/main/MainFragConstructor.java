package com.ytempest.wanandroid.activity.main;

import androidx.fragment.app.Fragment;

import com.ytempest.tool.helper.ExpandFragHelper;
import com.ytempest.wanandroid.activity.main.home.HomeFrag;
import com.ytempest.wanandroid.activity.main.knowledge.KnowledgeFrag;
import com.ytempest.wanandroid.activity.main.navigation.NavigationFrag;
import com.ytempest.wanandroid.activity.main.project.ProjectFrag;

/**
 * @author heqidu
 * @since 2020/6/24
 */
public class MainFragConstructor implements ExpandFragHelper.FragConstructor {
    @Override
    public Fragment createFrag(@MainTab.Id int fragId) {
        switch (fragId) {
            case MainTab.Id.HOME:
                return new HomeFrag();

            case MainTab.Id.KNOWLEDGE:
                return new KnowledgeFrag();

            case MainTab.Id.NAVIGATION:
                return new NavigationFrag();

            case MainTab.Id.PROJECT:
                return new ProjectFrag();

            default:
                throw new IllegalStateException("Not found the fragment for id : " + fragId);
        }
    }
}
