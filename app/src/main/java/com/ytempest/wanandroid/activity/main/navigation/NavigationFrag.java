package com.ytempest.wanandroid.activity.main.navigation;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.ytempest.layoutinjector.annotation.InjectLayout;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.base.fragment.LoaderFrag;
import com.ytempest.wanandroid.base.load.ViewType;
import com.ytempest.wanandroid.http.bean.NavigationListBean;
import com.ytempest.wanandroid.widget.VerticalTabLayout;

import java.util.List;

import butterknife.BindView;

/**
 * @author heqidu
 * @since 2020/6/23
 */
@InjectLayout(R.layout.frag_navigation)
public class NavigationFrag extends LoaderFrag<NavaigationPresenter> implements INavigationContract.View {

    private static final String TAG = NavigationFrag.class.getSimpleName();

    @BindView(R.id.group_navigation_title_list)
    VerticalTabLayout mTitleTabLayout;
    @BindView(R.id.group_navigation_content_list)
    RecyclerView mContentListView;
    private ContentAdapter mContentAdapter;
    private LinearLayoutManager mContentManager;
    private boolean isFromTab;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleTabLayout.addTabActonListener(new VerticalTabLayout.TabActonListener() {
            @Override
            public void onTabClick(View view, int position) {
                scrollContentToPosition(position, true);
            }

            @Override
            public void onTabSelected(View view, int position) {
                view.setSelected(true);
            }

            @Override
            public void onTabUnselected(View view, int position) {
                view.setSelected(false);
            }
        });

        mContentListView.setHasFixedSize(true);
        mContentListView.setLayoutManager(mContentManager = new LinearLayoutManager(getContext()));
        mContentListView.setAdapter(mContentAdapter = new ContentAdapter(mPresenter));
        mContentListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !mContentAdapter.isEmpty()) {
                    if (isFromTab) {
                        // 重置数据
                        isFromTab = false;

                    } else {
                        int firstVisiblePosition = mContentManager.findFirstVisibleItemPosition();
                        mTitleTabLayout.smoothScrollToPosition(firstVisiblePosition);
                    }
                }
            }
        });
        loadData();
    }

    private void loadData() {
        getLoader().showView(ViewType.LOAD);
        mPresenter.getNavigationList();
    }

    private void scrollContentToPosition(int pos, boolean isFromTab) {
        this.isFromTab = isFromTab;
        mContentListView.smoothScrollToPosition(pos);
    }

    @Override
    public void displayNavigationList(List<NavigationListBean> list) {
        getLoader().hideAll();
        mTitleTabLayout.setAdapter(new TitleAdapter(list));
        mContentAdapter.display(list);
    }

    @Override
    public void onNavigationListFail(int code) {
        getLoader().showView(ViewType.ERR);
    }

    @Override
    protected void onReloadClick() {
        super.onReloadClick();
        loadData();
    }

    @Override
    public void onNavigationArticleCollectSuccess(NavigationListBean.ArticlesBean article) {
        showToast(R.string.collect_success);
    }

    @Override
    public void onNavigationArticleCollectFail(int code, boolean onceCollected, NavigationListBean.ArticlesBean article) {
        showToast(onceCollected ? R.string.once_collected : R.string.collect_fail);
    }
}
