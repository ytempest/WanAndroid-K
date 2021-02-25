package com.ytempest.wanandroid.activity.main.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.ytempest.wanandroid.base.fragment.LoaderFrag
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.layoutinjector.annotation.InjectLayout
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean
import kotlinx.android.synthetic.main.frag_project.*

/**
 * @author heqidu
 * @since 21-2-10
 */
@InjectLayout(R.layout.frag_project)
class ProjectFrag : LoaderFrag<ProjectPresenter>(), IProjectView {

    private lateinit var mAdapter: ProjectClassifyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            mAdapter = ProjectClassifyAdapter(activity!!.supportFragmentManager)
            vp_project_content.offscreenPageLimit = 3
            vp_project_content.adapter = mAdapter

            group_project_tab.setupWithViewPager(vp_project_content)
            group_project_tab.tabMode = TabLayout.MODE_SCROLLABLE
            group_project_tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) = setSelectedTab(tab, true)

                override fun onTabReselected(tab: TabLayout.Tab?) = setSelectedTab(tab, false)

                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            })
        }
        getLoader().showView(ViewType.LOAD)
        mPresenter.getProjectClassify()
    }

    private fun setSelectedTab(tab: TabLayout.Tab?, selected: Boolean) {
        tab?.customView?.let {
            it.findViewById<View>(R.id.tv_tab_classify_content).isSelected = selected
        }
    }

    override fun onProjectClassifyReceived(list: List<ProjectClassifyBean>) {
        getLoader().hideAll()
        mAdapter.display(list)
        updateTabView(list)
    }

    private fun updateTabView(list: List<ProjectClassifyBean>) {
        val inflater = LayoutInflater.from(context)
        for ((idx, bean) in list.withIndex()) {
            val tab = group_project_tab.getTabAt(idx)
            val view = inflater.inflate(R.layout.item_tab_classify_content, null, false)
            view.findViewById<TextView>(R.id.tv_tab_classify_content).text = bean.name
            tab?.customView = view
        }
        val firstTab = group_project_tab.getTabAt(0)
        setSelectedTab(firstTab, true)
    }

    override fun onProjectClassifyFail(code: Int) {
        getLoader().showView(ViewType.ERR)
    }

}