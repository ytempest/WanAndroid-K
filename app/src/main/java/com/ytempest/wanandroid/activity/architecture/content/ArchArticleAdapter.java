package com.ytempest.wanandroid.activity.architecture.content;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ytempest.tool.adapter.CoreRecyclerAdapter;
import com.ytempest.tool.adapter.CoreViewHolder;
import com.ytempest.tool.helper.ActivityLauncher;
import com.ytempest.tool.util.ToastUtils;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.activity.article.ArticleDetailActivity;
import com.ytempest.wanandroid.activity.login.LoginActivity;
import com.ytempest.wanandroid.http.bean.ArchitectureContentBean;
import com.ytempest.wanandroid.http.bean.ArticleDetailBean;
import com.ytempest.wanandroid.utils.DateFormat;

/**
 * @author heqidu
 * @since 2021/1/14
 */
public class ArchArticleAdapter extends CoreRecyclerAdapter<ArchitectureContentBean.DatasBean> {

    private final ArchArticlePresenter mPresenter;

    public ArchArticleAdapter(ArchArticlePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected CoreViewHolder onCreateView(LayoutInflater inflater, ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.item_arch_content, viewGroup, false);
        CoreViewHolder holder = new CoreViewHolder(view);
        holder.setNeedClick(true);
        return holder;
    }

    private final View.OnClickListener mCollectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPresenter.isUserLogin()) {
                ArchitectureContentBean.DatasBean article = (ArchitectureContentBean.DatasBean) view.getTag();
                boolean newCollectStatus = !article.isCollect();
                mPresenter.updateArticleCollectStatus(newCollectStatus, article);

            } else {
                Context context = view.getContext();
                ToastUtils.show(context, R.string.login_please);
                ActivityLauncher.startActivity(context, new Intent(context, LoginActivity.class));
            }
        }
    };

    @Override
    protected void onBindData(CoreViewHolder holder, ArchitectureContentBean.DatasBean data, int position) {
        String user = data.getShareUser();
        user = TextUtils.isEmpty(user) ? holder.getRootView().getResources().getString(R.string.anonymous_develop) : user;
        holder.setText(R.id.tv_item_arch_content_user, user);

        TextView collectView = holder.getViewById(R.id.tv_item_arch_content_collect_status);
        collectView.setTag(data);
        collectView.setOnClickListener(mCollectClickListener);
        collectView.setText(data.isCollect() ? R.string.cancel_collect : R.string.collect);

        holder.setText(R.id.tv_item_arch_content_content, data.getTitle());
        holder.setText(R.id.tv_item_arch_content_time, DateFormat.format(data.getPublishTime()));
    }

    @Override
    protected void onItemClick(CoreViewHolder holder, View view, int position) {
        super.onItemClick(holder, view, position);
        ArchitectureContentBean.DatasBean data = getData(position);
        ArticleDetailActivity.start(view.getContext(), ArticleDetailBean.from(data));
    }
}
