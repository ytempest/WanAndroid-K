package com.ytempest.wanandroid.activity.architecture.content;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.ytempest.layoutinjector.annotation.InjectLayout;
import com.ytempest.tool.util.LogUtils;
import com.ytempest.tool.util.ToastUtils;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.base.fragment.LoaderFrag;
import com.ytempest.wanandroid.base.load.ViewType;
import com.ytempest.wanandroid.helper.ArticleDetailHelper;
import com.ytempest.wanandroid.http.bean.ArchitectureContentBean;
import com.ytempest.wanandroid.http.bean.ArticleDetailBean;
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean;
import com.ytempest.wanandroid.utils.JSON;
import com.ytempest.wanandroid.utils.Utils;

import butterknife.BindView;

/**
 * @author heqidu
 * @since 2021/1/6
 */
@InjectLayout(R.layout.frag_arch_content)
public class ArchArticleFrag extends LoaderFrag<ArchArticlePresenter> implements IArchArticleContract.View {

    private static final String TAG = ArchArticleFrag.class.getSimpleName();

    private static final String KEY_ARCH_CONTENT = "arch_content";

    public static ArchArticleFrag newInstance(KnowledgeArchitectureBean.ChildrenBean data) {
        ArchArticleFrag frag = new ArchArticleFrag();
        Bundle bundle = frag.getBundle();
        bundle.putString(KEY_ARCH_CONTENT, JSON.toJson(data));
        return frag;
    }

    @BindView(R.id.group_arch_content_list)
    RecyclerView mRecyclerView;
    private KnowledgeArchitectureBean.ChildrenBean mBean;
    private ArchArticleAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String json = getBundle().getString(KEY_ARCH_CONTENT, null);
        mBean = JSON.from(json, KnowledgeArchitectureBean.ChildrenBean.class);
        if (mBean == null) {
            ToastUtils.show(getContext(), R.string.unknown_err);
            return;
        }

        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ArchArticleAdapter(mPresenter);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    boolean arriveBottom = Utils.isArriveBottom(recyclerView);
                    boolean arriveTop = Utils.isArriveTop(recyclerView);
                    if (arriveTop && arriveBottom) {
                        LogUtils.d(TAG, "onScrollStateChanged: 已经展示所有数据");
                        return;
                    }

                    if (!mAdapter.isEmpty() && arriveBottom) {
                        mPresenter.loadMoreArchitectureContent(mBean.getId());
                    }
                }
            }
        });
        ArticleDetailHelper.getInstance().getArticleUpdateDetail().observe(getViewLifecycleOwner(), bean -> {
            if (bean == null || bean.getSource() != ArticleDetailBean.Source.KNOWLEDGE) return;
            for (ArchitectureContentBean.DatasBean data : mAdapter.getSrcDataList()) {
                if (data.getId() == bean.getArticleId()) {
                    data.setCollect(bean.isCollected());
                    mAdapter.refresh(data);
                    return;
                }
            }
        });

        getLoader().showView(ViewType.LOAD);
        mPresenter.refreshArchitectureContent(mBean.getId());
    }

    @Override
    protected void onReloadClick() {
        super.onReloadClick();
        mPresenter.refreshArchitectureContent(mBean.getId());
    }

    @Override
    public void onRefreshArchitectureFail(int code) {
        getLoader().showView(ViewType.ERR);
    }

    @Override
    public void onLoadArchitectureContent(ArchitectureContentBean content, boolean fromRefresh) {
        getLoader().hideAll();
        if (fromRefresh) {
            mAdapter.display(content.getDatas());

        } else {
            if (content.isOver()) {
                showToast(R.string.arrived_end);
            } else {
                mAdapter.addData(content.getDatas());
            }
        }
    }

    @Override
    public void onArchArticleCollectSuccess(boolean isCollect, ArchitectureContentBean.DatasBean article) {
        article.setCollect(isCollect);
        mAdapter.refresh(article);
    }

    @Override
    public void onArchArticleCollectFail(boolean isCollect, ArchitectureContentBean.DatasBean article, int code) {
        showToast(isCollect ? R.string.collect_fail : R.string.cancel_fail);
    }
}
