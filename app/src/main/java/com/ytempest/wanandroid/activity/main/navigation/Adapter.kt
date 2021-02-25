package com.ytempest.wanandroid.activity.main.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ytempest.wanandroid.activity.article.ArticleDetailActivity
import com.ytempest.wanandroid.ext.ctx
import com.ytempest.tool.adapter.CoreRecyclerAdapter
import com.ytempest.tool.adapter.CoreViewHolder
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.http.bean.ArticleDetailBean
import com.ytempest.wanandroid.http.bean.NavigationListBean
import com.ytempest.wanandroid.widget.TabFlowLayout
import com.ytempest.wanandroid.widget.VerticalTabLayout

/**
 * @author heqidu
 * @since 21-2-9
 */

class TitleAdapter(list: MutableList<NavigationListBean>) : VerticalTabLayout.Adapter<NavigationListBean>(list) {
    override fun createTabView(parent: ViewGroup, inflater: LayoutInflater, item: NavigationListBean, position: Int): View {
        val view = inflater.inflate(R.layout.item_navigation_title, parent, false)
        view.findViewById<TextView>(R.id.tv_item_navigation_title).text = item.name
        return view
    }
}

class ContentAdapter(
        private val mPresenter: NavigationPresenter
) : CoreRecyclerAdapter<NavigationListBean>() {

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup, position: Int): CoreViewHolder =
            CoreViewHolder(inflater.inflate(R.layout.item_navigation_content, viewGroup, false))

    private val mArticleTabClickListener: View.OnClickListener = View.OnClickListener {
        val article = it.tag as NavigationListBean.Articles
        ArticleDetailActivity.start(it.ctx, ArticleDetailBean.from(article))
    }

    private val mArticleTabLongClickListener: View.OnLongClickListener = View.OnLongClickListener {
        val article = it.tag as NavigationListBean.Articles
        mPresenter.addCollectOutsideArticle(article)
        true
    }

    override fun onBindData(holder: CoreViewHolder, data: NavigationListBean, position: Int) {
        holder.setText(R.id.tv_item_navigation_content, "—— ${data.name} ——")
        val layout = holder.getViewById<TabFlowLayout>(R.id.group_navigation_content_container)
        layout.setViewBinder(object : TabFlowLayout.ViewBinder<NavigationListBean.Articles>(data.articles) {

            override fun onCreateView(inflater: LayoutInflater, layout: TabFlowLayout): View =
                    inflater.inflate(R.layout.item_navigation_content_detail, layout, false)

            override fun onBindView(layout: TabFlowLayout, view: View, data: NavigationListBean.Articles, index: Int) {
                with(view.findViewById<TextView>(R.id.tv_item_navigation_content_detail)) {
                    setOnClickListener(mArticleTabClickListener)
                    setOnLongClickListener(mArticleTabLongClickListener)
                    tag = data
                    text = data.title
                }
            }
        })
    }
}