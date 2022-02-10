package com.ytempest.wanandroid.activity.main.project.content.list

import android.content.*
import android.net.Uri
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
import com.ytempest.wanandroid.activity.main.project.content.ProjectContentViewModel
import com.ytempest.wanandroid.ext.ctx
import com.ytempest.wanandroid.ext.getString
import com.ytempest.wanandroid.http.bean.ArticleDetailBean
import com.ytempest.wanandroid.http.bean.ProjectContentBean
import com.ytempest.wanandroid.utils.DateFormat
import com.ytempest.wanandroid.utils.ImgLoader

/**
 * @author heqidu
 * @since 21-2-10
 */
class ContentListAdapter(
        private val mPresenter: ProjectContentViewModel
) : CoreRecyclerAdapter<ProjectContentBean.Data>() {

    private val mCollectClickListener = View.OnClickListener {
        val data = it.tag as ProjectContentBean.Data
        mPresenter.addProjectArticleCollect(data)
    }

    private val mVcsClickListener = View.OnClickListener {
        val url = Uri.parse(it.tag as String)
        ActivityLauncher.startActivity(it.ctx, Intent(Intent.ACTION_VIEW, url))
    }

    private val mVcsLongClickListener = View.OnLongClickListener {
        val manager = it.ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val projectUrl = it.tag as String
        manager.setPrimaryClip(ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_PLAIN, projectUrl.trim()))
        ToastUtils.show(it.ctx, R.string.copy_to_clipboard)
        true
    }

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup, position: Int): CoreViewHolder {
        val view = inflater.inflate(R.layout.item_project_content_list, viewGroup, false)
        val holder = CoreViewHolder(view)
        holder.isNeedClick = true
        return holder
    }

    override fun onBindData(holder: CoreViewHolder, data: ProjectContentBean.Data, position: Int) {
        val imgView = holder.getViewById<ImageView>(R.id.iv_item_project_content_img)
        ImgLoader.loadTo(imgView, data.envelopePic)

        with(holder) {
            val user = if (!data.shareUser.isNullOrEmpty()) data.shareUser
            else holder.itemView.getString(R.string.anonymous_develop)
            setText(R.id.tv_item_project_content_user, user)
            setText(R.id.tv_item_project_content_title, data.title)
            setText(R.id.tv_item_project_content_desc, data.desc)
            setText(R.id.tv_item_project_content_time, DateFormat.format(data.publishTime))
        }

        with(holder.getViewById<View>(R.id.tv_item_project_content_vcs_url)) {
            tag = data.projectLink
            setOnClickListener(mVcsClickListener)
            setOnLongClickListener(mVcsLongClickListener)
        }

        // TODO : ProjectContentBean.Data#isCollect()这个方法未能获取正确的收藏状态，需要等服务器支持后再做处理
        val collectView = holder.getViewById<View>(R.id.tv_item_project_content_collect)
        collectView.tag = data
        collectView.setOnClickListener(mCollectClickListener)
    }

    override fun onItemClick(holder: CoreViewHolder, view: View, position: Int) {
        super.onItemClick(holder, view, position)
        val data = getData(position)
        ArticleDetailActivity.start(view.ctx, ArticleDetailBean.from(data))
    }
}