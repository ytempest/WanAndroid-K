package com.ytempest.wanandroid.activity.main.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.activity.main.home.article.HomeArticleAdapter
import com.ytempest.wanandroid.base.fragment.LoaderFrag
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.databinding.FragHomeBinding
import com.ytempest.wanandroid.helper.ArticleDetailHelper
import com.ytempest.wanandroid.http.bean.ArticleDetailBean
import com.ytempest.wanandroid.http.bean.BannerBean
import com.ytempest.wanandroid.http.bean.HomeArticleBean

/**
 * @author heqidu
 * @since 21-2-9
 */
class HomeFrag : LoaderFrag<HomePresenter, FragHomeBinding>(), IHomeView {

    private val mAdapter by lazy { HomeArticleAdapter(mPresenter) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        binding.refreshView.setColorSchemeResources(R.color.main_color)
        binding.refreshView.setOnRefreshListener { mPresenter.refreshHomeArticle() }

        binding.bannerView.run {
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
                        mPresenter.loadMoreHomeArticle()
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
        getLoader().showView(ViewType.LOAD)
        mPresenter.loadHomeData()
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
    }

    override fun onReloadClick() {
        super.onReloadClick()
        mPresenter.loadHomeData()
    }

    override fun onHomeDataSuccess(bannerList: List<BannerBean>, bean: HomeArticleBean) {
        getLoader().hideAll()
        binding.bannerView.display(bannerList)
        mAdapter.display(bean.datas)

    }

    override fun onHomeDataFail(code: Int) {
        getLoader().showView(ViewType.ERR)
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
        showToast(if (data.collect) R.string.collect_fail else R.string.cancel_fail)
    }
}