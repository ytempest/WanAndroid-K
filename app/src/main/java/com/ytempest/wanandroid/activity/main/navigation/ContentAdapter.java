package com.ytempest.wanandroid.activity.main.navigation;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ytempest.tool.adapter.CoreRecyclerAdapter;
import com.ytempest.tool.adapter.CoreViewHolder;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.activity.article.ArticleDetailActivity;
import com.ytempest.wanandroid.http.bean.ArticleDetailBean;
import com.ytempest.wanandroid.http.bean.NavigationListBean;
import com.ytempest.wanandroid.widget.TabFlowLayout;

/**
 * @author heqidu
 * @since 2020/12/29
 */
public class ContentAdapter extends CoreRecyclerAdapter<NavigationListBean> {

    private static final String TAG = ContentAdapter.class.getSimpleName();

    private final NavaigationPresenter mPresenter;

    public ContentAdapter(NavaigationPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected CoreViewHolder onCreateView(LayoutInflater inflater, ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.item_navigation_content, viewGroup, false);
        return new CoreViewHolder(view);
    }

    private final View.OnClickListener mArticleTabClickListener = v -> {
        NavigationListBean.ArticlesBean article = (NavigationListBean.ArticlesBean) v.getTag();
        ArticleDetailActivity.start(v.getContext(), ArticleDetailBean.from(article));
    };

    private final View.OnLongClickListener mArticleTabLongClickListener = v -> {
        NavigationListBean.ArticlesBean article = (NavigationListBean.ArticlesBean) v.getTag();
        ContentAdapter.this.mPresenter.addCollectOutsideArticle(article);
        return true;
    };

    @Override
    protected void onBindData(CoreViewHolder holder, NavigationListBean data, int position) {
        holder.setText(R.id.tv_item_navigation_content, String.format("—— %s ——", data.getName()));
        TabFlowLayout layout = holder.getViewById(R.id.group_navigation_content_container);
        layout.setViewBinder(new TabFlowLayout.ViewBinder<NavigationListBean.ArticlesBean>(data.getArticles()) {
            @NonNull
            @Override
            protected View onCreateView(LayoutInflater inflater, TabFlowLayout layout) {
                return inflater.inflate(R.layout.item_navigation_content_detail, layout, false);
            }

            @Override
            protected void onBindView(TabFlowLayout layout, View view, NavigationListBean.ArticlesBean article, int index) {
                TextView tabView = view.findViewById(R.id.tv_item_navigation_content_detail);
                tabView.setTag(article);
                tabView.setOnClickListener(mArticleTabClickListener);
                tabView.setOnLongClickListener(mArticleTabLongClickListener);
                tabView.setText(article.getTitle());
            }
        });
    }

}
