package com.ytempest.wanandroid.activity.main.home.article

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ytempest.tool.adapter.CoreRecyclerAdapter
import com.ytempest.tool.adapter.CoreViewHolder
import com.ytempest.tool.helper.ActivityLauncher
import com.ytempest.tool.util.ToastUtils
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.activity.article.ArticleDetailActivity
import com.ytempest.wanandroid.activity.login.LoginActivity
import com.ytempest.wanandroid.activity.main.home.HomeViewModel
import com.ytempest.framework.ext.ctx
import com.ytempest.wanandroid.http.bean.ArticleDetailBean
import com.ytempest.wanandroid.http.bean.HomeArticleBean
import com.ytempest.wanandroid.utils.DateFormat

/**
 * @author heqidu
 * @since 21-2-9
 */
class HomeArticleAdapter(
        private val viewModel: HomeViewModel
) : CoreRecyclerAdapter<HomeArticleBean.Data>() {

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup, position: Int): CoreViewHolder {
        val view = inflater.inflate(R.layout.item_home_article_content, viewGroup, false)
        return CoreViewHolder(view).also {
            it.isNeedClick = true
        }
    }

    private val mOnCollectClickListener = View.OnClickListener {
        if (viewModel.isUserLogin()) {
            val data = it.tag as HomeArticleBean.Data
            viewModel.updateArticleCollectStatus(data)

        } else {
            ToastUtils.show(it.ctx, R.string.login_please)
            ActivityLauncher.startActivity(it.ctx, Intent(it.ctx, LoginActivity::class.java))
        }
    }

    override fun onBindData(holder: CoreViewHolder, data: HomeArticleBean.Data, position: Int) {
        holder.run {
            tag = data
            val user = if (!TextUtils.isEmpty(data.shareUser)) data.shareUser
            else recyclerView.resources.getString(R.string.anonymous_develop)
            setText(R.id.tv_item_home_article_user, user);
            setText(R.id.tv_item_home_article_classify, "${data.superChapterName}/${data.chapterName}")
            setText(R.id.tv_item_home_article_content, data.title)
            setText(R.id.tv_item_home_article_time, DateFormat.format(data.shareDate))
        }

        val collectView = holder.getViewById<ImageView>(R.id.iv_item_home_article_collect)
        collectView.run {
            tag = data
            isSelected = data.collect
            setOnClickListener(mOnCollectClickListener)
        }
    }

    override fun onItemClick(holder: CoreViewHolder, view: View, position: Int) {
        super.onItemClick(holder, view, position)
        val data = holder.tag as HomeArticleBean.Data
        ArticleDetailActivity.start(view.ctx, ArticleDetailBean.from(data))
    }
}