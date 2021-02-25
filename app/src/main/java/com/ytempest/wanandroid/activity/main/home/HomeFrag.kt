package com.ytempest.wanandroid.activity.main.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ytempest.wanandroid.activity.main.home.article.HomeArticleAdapter
import com.ytempest.wanandroid.base.fragment.LoaderFrag
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.helper.ArticleDetailHelper
import com.ytempest.layoutinjector.annotation.InjectLayout
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.http.bean.ArticleDetailBean
import com.ytempest.wanandroid.http.bean.BannerBean
import com.ytempest.wanandroid.http.bean.HomeArticleBean
import com.ytempest.wanandroid.http.bean.HomeArticleBean.Data
import kotlinx.android.synthetic.main.frag_home.*
import kotlinx.android.synthetic.main.frag_home_content.*

/**
 * @author heqidu
 * @since 21-2-9
 */
@InjectLayout(R.layout.frag_home)
class HomeFrag : LoaderFrag<HomePresenter>(), IHomeView {

    private lateinit var mAdapter: HomeArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        view_main_pull_refresh.setColorSchemeResources(R.color.main_color)
        view_main_pull_refresh.setOnRefreshListener { mPresenter.refreshHomeArticle() }

        with(view_main_banner) {
            setBannerBinder(HomeBannerBinder())
            setPlayDuration(3000)
            setScrollDuration(1500)
        }

        mAdapter = HomeArticleAdapter(mPresenter)
        with(rv_main_article_list) {
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
        val isArriveBottom = !view_main_nested_scroll.canScrollVertically(1)
        val isIdleState = rv_main_article_list.scrollState == RecyclerView.SCROLL_STATE_IDLE
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
        view_main_banner.display(bannerList)
        mAdapter.display(bean.datas)

    }

    override fun onHomeDataFail(code: Int) {
        getLoader().showView(ViewType.ERR)
    }

    override fun displayHomeArticle(fromRefresh: Boolean, homeArticleBean: HomeArticleBean) {
        if (fromRefresh) {
            view_main_pull_refresh.isRefreshing = false
            mAdapter.display(homeArticleBean.datas)

        } else {
            mAdapter.addData(homeArticleBean.datas)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) view_main_banner.stopAutoPlay()
        else view_main_banner.startAutoPlay()
    }

    override fun onResume() {
        super.onResume()
        view_main_banner.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        view_main_banner.stopAutoPlay()
    }

    override fun onArticleCollectSuccess(data: HomeArticleBean.Data) {
        mAdapter.refresh(data)
    }


    override fun onArticleCollectFail(data: HomeArticleBean.Data, code: Int) {
        showToast(if (data.collect) R.string.collect_fail else R.string.cancel_fail)
    }
}