package com.ytempest.wanandroid.activity.main.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.base.fragment.LoaderFrag
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.databinding.FragProjectBinding
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean

/**
 * @author heqidu
 * @since 21-2-10
 */
class ProjectFrag : LoaderFrag<ProjectPresenter, FragProjectBinding>(), IProjectView {

    private lateinit var mAdapter: ProjectClassifyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            mAdapter = ProjectClassifyAdapter(activity!!.supportFragmentManager)
            binding.pageView.offscreenPageLimit = 3
            binding.pageView.adapter = mAdapter

            binding.tabView.setupWithViewPager(binding.pageView)
            binding.tabView.tabMode = TabLayout.MODE_SCROLLABLE
            binding.tabView.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
            val tab = binding.tabView.getTabAt(idx)
            val view = inflater.inflate(R.layout.item_tab_classify_content, null, false)
            view.findViewById<TextView>(R.id.tv_tab_classify_content).text = bean.name
            tab?.customView = view
        }
        val firstTab = binding.tabView.getTabAt(0)
        setSelectedTab(firstTab, true)
    }

    override fun onProjectClassifyFail(code: Int) {
        getLoader().showView(ViewType.ERR)
    }

}