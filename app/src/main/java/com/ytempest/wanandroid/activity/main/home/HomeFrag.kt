package com.ytempest.wanandroid.activity.main.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.activity.main.home.article.HomeArticleAdapter
import com.ytempest.wanandroid.base.createViewModel
import com.ytempest.wanandroid.base.fragment.MVVMFragment
import com.ytempest.wanandroid.base.load.Loader
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.base.vm.EntityObserver
import com.ytempest.wanandroid.databinding.FragHomeBinding
import com.ytempest.wanandroid.helper.ArticleDetailHelper
import com.ytempest.wanandroid.http.bean.ArticleDetailBean
import com.ytempest.wanandroid.http.bean.BannerBean
import com.ytempest.wanandroid.http.bean.HomeArticleBean

/**
 * @author heqidu
 * @since 21-2-9
 */
class HomeFrag : MVVMFragment<FragHomeBinding>(), IHomeView {

    override val viewModel by lazy { createViewModel<HomeViewModel>() }
    private val mAdapter by lazy { HomeArticleAdapter(viewModel) }

    private val loader: Loader by lazy {
        Loader(binding.root as ViewGroup).also {
            it.setReloadCall {
                it.showView(ViewType.LOAD)
                viewModel.loadHomeData()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        binding.refreshView.setColorSchemeResources(R.color.main_color)
        binding.refreshView.setOnRefreshListener { viewModel.refreshHomeArticle() }

        binding.bannerView.apply {
            setBannerBinder(HomeBannerBinder())
            setPlayDuration(3000)
            setScrollDuration(1500)
        }

        binding.articlesView.run {
            itemAnimator = null
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            isNestedScrollingEnabled = true
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (isArriveBottom()) {
                        viewModel.loadMoreHomeArticle()
                    }
                }
            })
        }
    }

    private fun isArriveBottom(): Boolean {
        // RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
        // RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        // 由于NestedScrollView嵌套了RecyclerView，这里需要通过NestedScrollView判断
        val isArriveBottom = !binding.nestedScrollView.canScrollVertically(1)
        val isIdleState = binding.articlesView.scrollState == RecyclerView.SCROLL_STATE_IDLE
        return isArriveBottom && isIdleState
    }

    private fun initData() {
        loader.showView(ViewType.LOAD)
        ArticleDetailHelper.instance.getArticleUpdateDetail().observe(viewLifecycleOwner, Observer {
            if (it == null || it.source != ArticleDetailBean.Source.HOME) {
                return@Observer;
            }
            for (data in mAdapter.srcDataList) {
                if (data.id == it.articleId) {
                    data.collect = it.isCollected
                    mAdapter.refresh(data)
                }
            }
        })

        viewModel.homeDataResult.observe(this, EntityObserver(
                onSuccess = { entity ->
                    val (bannerList, bean) = entity.data
                    onHomeDataSuccess(bannerList, bean)
                },
                onFail = { entity ->
                    onHomeDataFail(entity.code)
                }
        ))

        viewModel.homeArticlesResult.observe(this, EntityObserver(
                onSuccess = { entity ->
                    val (fromRefresh, bean) = entity.data
                    displayHomeArticle(fromRefresh, bean)
                }
        ))

        viewModel.homeArticleCollectResult.observe(this, EntityObserver(
                onSuccess = { entity ->
                    onArticleCollectSuccess(entity.data)
                },
                onFail = { entity ->
                    onArticleCollectFail(entity.extra as HomeArticleBean.Data, entity.code)
                }
        ))

        viewModel.loadHomeData()
    }

    override fun onHomeDataSuccess(bannerList: List<BannerBean>, bean: HomeArticleBean) {
        loader.hideAll()
        binding.bannerView.display(bannerList)
        mAdapter.display(bean.datas)
    }

    override fun onHomeDataFail(code: Int) {
        loader.showView(ViewType.ERR)
    }

    override fun displayHomeArticle(fromRefresh: Boolean, homeArticleBean: HomeArticleBean) {
        if (fromRefresh) {
            binding.refreshView.isRefreshing = false
            mAdapter.display(homeArticleBean.datas)

        } else {
            mAdapter.addData(homeArticleBean.datas)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) binding.bannerView.stopAutoPlay()
        else binding.bannerView.startAutoPlay()
    }

    override fun onResume() {
        super.onResume()
        binding.bannerView.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        binding.bannerView.stopAutoPlay()
    }

    override fun onArticleCollectSuccess(data: HomeArticleBean.Data) {
        mAdapter.refresh(data)
    }


    override fun onArticleCollectFail(data: HomeArticleBean.Data, code: Int) {
        showToast(if (!data.collect) R.string.collect_fail else R.string.cancel_fail)
    }
}