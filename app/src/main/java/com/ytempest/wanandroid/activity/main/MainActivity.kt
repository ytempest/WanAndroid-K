package com.ytempest.wanandroid.activity.main

import androidx.fragment.app.Fragment
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.ytempest.layoutinjector.annotation.InjectLayout
import com.ytempest.tool.helper.ExpandFragHelper
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.base.activity.MvpActivity
import kotlinx.android.synthetic.main.activity_main_content.*

/**
 * @author heqidu
 * @since 21-2-8
 */
@InjectLayout(R.layout.activity_main)
class MainActivity : MvpActivity<MainPresenter>(), IMainView {

    private val mFragHelper: ExpandFragHelper by lazy { ExpandFragHelper(supportFragmentManager, MainFragConstructor()) }
    private var mCurFrag: Fragment? = null

    override fun onViewCreated() {
        super.onViewCreated()
        initTabs()
    }

    private fun initTabs() {
        navigationBar_main_content.clearAll()
        navigationBar_main_content.setTabSelectedListener(object : BottomNavigationBar.SimpleOnTabSelectedListener() {
            override fun onTabSelected(index: Int) {
                super.onTabSelected(index)
                val tab = MainTab.values()[index]
                switchFrag(tab.id)
            }
        })

        for (tab in MainTab.values()) {
            navigationBar_main_content.addItem(BottomNavigationItem(tab.selectIcon, tab.title)
                    .setInactiveIconResource(tab.normalIcon))
        }
        navigationBar_main_content.initialise()
        switchFrag(MainTab.Id.HOME)
    }

    private fun switchFrag(@MainTab.Id id: Int) {
        mCurFrag = mFragHelper.switchFrag(R.id.frameLayout_main_content_container, mCurFrag, id)
    }
}