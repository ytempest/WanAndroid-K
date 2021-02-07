package com.ytempest.wanandroid.activity.main.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.ytempest.banner.Banner;
import com.ytempest.banner.transformers.Transformers;
import com.ytempest.layoutinjector.annotation.InjectLayout;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.activity.main.home.article.HomeArticleAdapter;
import com.ytempest.wanandroid.base.fragment.LoaderFrag;
import com.ytempest.wanandroid.base.load.ViewType;
import com.ytempest.wanandroid.helper.ArticleDetailHelper;
import com.ytempest.wanandroid.http.bean.ArticleDetailBean;
import com.ytempest.wanandroid.http.bean.BannerBean;
import com.ytempest.wanandroid.http.bean.HomeArticleBean;

import java.util.List;

import butterknife.BindView;

/**
 * @author heqidu
 * @since 2020/6/23
 */
@InjectLayout(R.layout.frag_home)
public class HomeFrag extends LoaderFrag<HomePresenter> implements IHomeContract.View {

    private static final String TAG = HomeFrag.class.getSimpleName();

    @BindView(R.id.view_main_pull_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.view_main_nested_scroll)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.view_main_banner)
    Banner mBannerView;
    @BindView(R.id.rv_main_article_list)
    RecyclerView mRecyclerView;
    private HomeArticleAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        mRefreshLayout.setColorSchemeResources(R.color.main_color);
        mRefreshLayout.setOnRefreshListener(() -> mPresenter.refreshHomeArticle());

        mBannerView.setBannerBinder(new HomeBannerBinder());
        mBannerView.setPlayDuration(3000);
        mBannerView.setScrollDuration(1500);
        mBannerView.setScrollAnimation(Transformers.DAMPING);

        // 移除RecyclerView更新数据时的动画，防止View闪烁的问题
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HomeArticleAdapter(mPresenter);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isArriveBottom()) {
                    mPresenter.loadMoreHomeArticle();
                }
            }
        });
    }

    private boolean isArriveBottom() {
        // RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
        // RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        // 由于NestedScrollView嵌套了RecyclerView，这里需要通过NestedScrollView判断
        boolean isArriveBottom = !mNestedScrollView.canScrollVertically(1);
        boolean isIdleState = mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE;
        return isArriveBottom && isIdleState;
    }

    private void initData() {
        getLoader().showView(ViewType.LOAD);
        mPresenter.loadHomeData();
        ArticleDetailHelper.getInstance().getArticleUpdateDetail().observe(getViewLifecycleOwner(), bean -> {
            if (bean == null || bean.getSource() != ArticleDetailBean.Source.HOME) return;
            for (HomeArticleBean.DatasBean data : mAdapter.getSrcDataList()) {
                if (data.getId() == bean.getArticleId()) {
                    data.setCollect(bean.isCollected());
                    mAdapter.refresh(data);
                    return;
                }
            }
        });
    }

    @Override
    protected void onReloadClick() {
        super.onReloadClick();
        mPresenter.loadHomeData();
    }

    @Override
    public void onHomeDataSuccess(List<BannerBean> bannerList, HomeArticleBean bean) {
        getLoader().hideAll();
        mBannerView.display(bannerList);
        mAdapter.display(bean.getDatas());
    }

    @Override
    public void onHomeDataFail(int code) {
        getLoader().showView(ViewType.ERR);
    }

    @Override
    public void displayHomeArticle(boolean fromRefresh, HomeArticleBean homeArticleBean) {
        if (fromRefresh) {
            mRefreshLayout.setRefreshing(false);
            mAdapter.display(homeArticleBean.getDatas());
        } else {
            mAdapter.addData(homeArticleBean.getDatas());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mBannerView.stopAutoPlay();
        } else {
            mBannerView.startAutoPlay();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mBannerView.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBannerView.stopAutoPlay();
    }

    @Override
    public void onArticleCollectSuccess(HomeArticleBean.DatasBean data) {
        mAdapter.refresh(data);
    }

    @Override
    public void onArticleCollectFail(HomeArticleBean.DatasBean data, int code) {
        showToast(data.isCollect() ? R.string.collect_fail : R.string.cancel_fail);
    }
}
