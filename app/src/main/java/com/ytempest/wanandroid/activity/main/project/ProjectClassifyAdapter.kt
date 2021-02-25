package com.ytempest.wanandroid.activity.main.project

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ytempest.wanandroid.activity.main.project.content.ProjectContentFrag

import com.ytempest.wanandroid.http.bean.ProjectClassifyBean
import com.ytempest.wanandroid.utils.CoreFragPagerAdapter

/**
 * @author heqidu
 * @since 21-2-10
 */
class ProjectClassifyAdapter(
        fm: FragmentManager
) : CoreFragPagerAdapter<ProjectClassifyBean>(fm) {

    override fun onCreateFragment(data: ProjectClassifyBean, pos: Int): Fragment {
        return ProjectContentFrag.newInstance(data);
    }
}