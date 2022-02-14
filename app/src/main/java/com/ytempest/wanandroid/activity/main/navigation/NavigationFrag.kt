package com.ytempest.wanandroid.activity.main.navigation

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ytempest.wanandroid.R
import com.ytempest.framework.base.createViewModel
import com.ytempest.framework.base.fragment.MVVMFragment
import com.ytempest.wanandroid.base.load.Loader
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.base.vm.EntityObserver
import com.ytempest.wanandroid.databinding.FragNavigationBinding
import com.ytempest.wanandroid.http.bean.NavigationListBean
import com.ytempest.wanandroid.widget.VerticalTabLayout

/**
 * @author heqidu
 * @since 21-2-9
 */
class NavigationFrag : MVVMFragment<FragNavigationBinding>(), INavigationView {

    override val viewModel by lazy { createViewModel<NavigationViewModel>() }
    private lateinit var mContentAdapter: ContentAdapter
    private lateinit var mContentManager: LinearLayoutManager
    private var isFromTab: Boolean = false

    private val loader: Loader by lazy {
        Loader(binding.root as ViewGroup).also {
            it.setReloadCall {
                it.showView(ViewType.LOAD)
                viewModel.loadNavigationList()
            }
        }
    }

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
        mContentAdapter = ContentAdapter(viewModel)
        binding.listView.run {
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
                            val firstVisiblePosition =
                                mContentManager.findFirstVisibleItemPosition()
                            binding.tabView.smoothScrollToPosition(firstVisiblePosition)
                        }
                    }
                }
            })
        }
        initData()
    }


    private fun initData() {
        viewModel.navigationListResult.observe(this, EntityObserver(
            onSuccess = { entity ->
                displayNavigationList(entity.data)
            },
            onFail = { entity ->
                onNavigationListFail(entity.code)
            }
        ))

        viewModel.outsideArticleCollectResult.observe(this, EntityObserver(
            onSuccess = { entity ->
                onNavigationArticleCollectSuccess(entity.data)
            },
            onFail = {
                val onceCollected = if (it.extra is Boolean) it.extra else false
                onNavigationArticleCollectFail(it.code, onceCollected)
            }
        ))

        loader.showView(ViewType.LOAD)
        viewModel.loadNavigationList()
    }

    private fun scrollContentToPosition(pos: Int, isFromTab: Boolean) {
        this.isFromTab = isFromTab
        binding.listView.smoothScrollToPosition(pos)
    }

    override fun displayNavigationList(list: List<NavigationListBean>) {
        loader.hideAll()
        binding.tabView.adapter = TitleAdapter(ArrayList(list))
        mContentAdapter.display(list)
    }

    override fun onNavigationListFail(code: Int) {
        loader.showView(ViewType.ERR)
    }

    override fun onNavigationArticleCollectSuccess(article: NavigationListBean.Articles) {
        showToast(R.string.collect_success)
    }

    override fun onNavigationArticleCollectFail(
        code: Int,
        onceCollected: Boolean,
    ) {
        showToast(if (onceCollected) R.string.once_collected else R.string.collect_fail)
    }
}