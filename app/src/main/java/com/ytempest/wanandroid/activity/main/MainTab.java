package com.ytempest.wanandroid.activity.main;

import androidx.annotation.IntDef;

import com.ytempest.wanandroid.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author heqidu
 * @since 2020/6/21
 */

public enum MainTab {

    // TAB的顺序通过这里的顺序控制
    HOME(Id.HOME, R.mipmap.ic_tab_home, R.mipmap.ic_tab_home_selected, R.string.tab_home),
    KNOWLEDGE(Id.KNOWLEDGE, R.mipmap.ic_tab_knowledge, R.mipmap.ic_tab_knowledge_selected, R.string.tab_knowledge),
    NAVIGATION(Id.NAVIGATION, R.mipmap.ic_tab_navigation, R.mipmap.ic_tab_navigation_selected, R.string.tab_navigation),
    PROJECT(Id.PROJECT, R.mipmap.ic_tab_project, R.mipmap.ic_tab_project_selected, R.string.tab_project),
    ;
    public int id;
    public int iconNormal;
    public int iconSelected;
    public int title;

    MainTab(@Id int id, int tabNormalIcon, int tabSelectIcon, int tabTitle) {
        this.id = id;
        this.iconNormal = tabNormalIcon;
        this.iconSelected = tabSelectIcon;
        this.title = tabTitle;
    }

    @IntDef({Id.HOME, Id.KNOWLEDGE, Id.NAVIGATION, Id.PROJECT,})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Id {
        int HOME = 0;
        int KNOWLEDGE = 1;
        int NAVIGATION = 2;
        int PROJECT = 3;
    }
}
