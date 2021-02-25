package com.ytempest.wanandroid.activity.architecture.content

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ytempest.tool.adapter.CoreRecyclerAdapter
import com.ytempest.tool.adapter.CoreViewHolder
import com.ytempest.tool.helper.ActivityLauncher
import com.ytempest.tool.util.ToastUtils
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.activity.article.ArticleDetailActivity
import com.ytempest.wanandroid.activity.login.LoginActivity
import com.ytempest.wanandroid.ext.ctx
import com.ytempest.wanandroid.ext.getString
import com.ytempest.wanandroid.http.bean.ArchitectureContentBean
import com.ytempest.wanandroid.http.bean.ArticleDetailBean
import com.ytempest.wanandroid.utils.DateFormat

/**
 * @author heqidu
 * @since 21-2-22
 */
class ArchArticleAdapter(
        private val mPresenter: ArchArticlePresenter
) : CoreRecyclerAdapter<ArchitectureContentBean.Data>() {

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, position: Int): CoreViewHolder? {
        val view = inflater.inflate(R.layout.item_arch_content, viewGroup, false)
        val holder = CoreViewHolder(view)
        holder.isNeedClick = true
        return holder
    }

    private val mCollectClickListener = View.OnClickListener {
        if (mPresenter.isUserLogin()) {
            val article = it.tag as ArchitectureContentBean.Data
            val newCollectStatus = !article.collect
            mPresenter.updateArticleCollectStatus(newCollectStatus, article)
        } else {
            ToastUtils.show(it.ctx, R.string.login_please);
            ActivityLauncher.startActivity(it.ctx, Intent(it.ctx, LoginActivity::class.java))
        }
    }

    override fun onBindData(holder: CoreViewHolder, data: ArchitectureContentBean.Data, position: Int) {
        val user = if (TextUtils.isEmpty(data.shareUser)) holder.itemView.getString(R.string.anonymous_develop)
        else data.shareUser
        holder.setText(R.id.tv_item_arch_content_user, user)
        holder.setText(R.id.tv_item_arch_content_content, data.title)
        holder.setText(R.id.tv_item_arch_content_time, DateFormat.format(data.publishTime))

        with(holder.getViewById<TextView>(R.id.tv_item_arch_content_collect_status)) {
            tag = data
            setOnClickListener(mCollectClickListener)
            setText(if (data.collect) R.string.cancel_collect else R.string.collect)
        }
    }

    override fun onItemClick(holder: CoreViewHolder, view: View, position: Int) {
        super.onItemClick(holder, view, position)
        val data = getData(position)
        ArticleDetailActivity.start(view.context, ArticleDetailBean.from(data))
    }
}