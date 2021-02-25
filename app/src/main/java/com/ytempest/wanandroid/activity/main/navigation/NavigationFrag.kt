package com.ytempest.wanandroid.activity.main.navigation

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.base.fragment.LoaderFrag
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.databinding.FragNavigationBinding
import com.ytempest.wanandroid.http.bean.NavigationListBean
import com.ytempest.wanandroid.widget.VerticalTabLayout

/**
 * @author heqidu
 * @since 21-2-9
 */
class NavigationFrag : LoaderFrag<NavigationPresenter, FragNavigationBinding>(), INavigationView {

    private lateinit var mContentAdapter: ContentAdapter
    private lateinit var mContentManager: LinearLayoutManager
    private var isFromTab: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tabView.addTabActonListener(object : VerticalTabLayout.TabActonListener {
            override fun onTabClick(view: View, position: Int) {
                scrollContentToPosition(position, false)
            }

            override fun onTabUnselected(view: View, position: Int) {
                view.isSelected = true
            }

            override fun onTabSelected(view: View, position: Int) {
                view.isSelected = false
            }
        })

        mContentManager = LinearLayoutManager(context)
        mContentAdapter = ContentAdapter(mPresenter)
        with(binding.listView) {
            setHasFixedSize(true)
            layoutManager = mContentManager
            adapter = mContentAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && !mContentAdapter.isEmpty) {
                        if (isFromTab) {
                            // 重置数据
                            isFromTab = false
                        } else {
                            val firstVisiblePosition = mContentManager.findFirstVisibleItemPosition()
                            binding.tabView.smoothScrollToPosition(firstVisiblePosition)
                        }
                    }
                }
            })
        }
        loadData()
    }

    override fun onReloadClick() {
        super.onReloadClick()
        loadData()
    }

    private fun loadData() {
        getLoader().showView(ViewType.LOAD)
        mPresenter.getNavigationList()
    }

    private fun scrollContentToPosition(pos: Int, isFromTab: Boolean) {
        this.isFromTab = isFromTab
        binding.listView.smoothScrollToPosition(pos)
    }

    override fun displayNavigationList(list: List<NavigationListBean>) {
        getLoader().hideAll()
        binding.tabView.adapter = TitleAdapter(ArrayList(list))
        mContentAdapter.display(list)
    }

    override fun onNavigationListFail(code: Int) {
        getLoader().showView(ViewType.ERR)
    }

    override fun onNavigationArticleCollectSuccess(article: NavigationListBean.Articles) {
        showToast(R.string.collect_success)
    }

    override fun onNavigationArticleCollectFail(code: Int, onceCollected: Boolean, article: NavigationListBean.Articles) {
        showToast(if (onceCollected) R.string.once_collected else R.string.collect_fail)
    }
}