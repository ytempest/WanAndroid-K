package com.ytempest.wanandroid.activity.architecture.content

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ytempest.layoutinjector.annotation.InjectLayout
import com.ytempest.tool.util.LogUtils
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.base.fragment.LoaderFrag
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.helper.ArticleDetailHelper
import com.ytempest.wanandroid.http.bean.ArchitectureContentBean
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean.Children
import com.ytempest.wanandroid.utils.JSON
import com.ytempest.wanandroid.utils.Utils
import com.ytempest.wanandroid.ext.getBundle
import com.ytempest.wanandroid.http.bean.ArticleDetailBean
import kotlinx.android.synthetic.main.frag_arch_content.*

/**
 * @author heqidu
 * @since 21-2-22
 */
@InjectLayout(R.layout.frag_arch_content)
class ArchArticleFrag : LoaderFrag<ArchArticlePresenter>(), IArchArticleView {

    private val TAG = "ArchArticleFrag"

    companion object {
        private const val KEY_ARCH_CONTENT = "arch_content"

        fun newInstance(data: Children): ArchArticleFrag {
            val frag = ArchArticleFrag()
            val bundle = frag.getBundle()
            bundle.putString(KEY_ARCH_CONTENT, JSON.toJson(data))
            return frag
        }
    }

    private lateinit var mAdapter: ArchArticleAdapter
    private lateinit var mBean: Children

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val json = getBundle().getString(KEY_ARCH_CONTENT, null)
        mBean = JSON.from(json, Children::class.java) as Children
        if (mBean == null) {
            showToast(R.string.unknown_err)
            return
        }

        mAdapter = ArchArticleAdapter(mPresenter)
        with(group_arch_content_list) {
            itemAnimator = null
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val arriveBottom = Utils.isArriveBottom(recyclerView)
                        val arriveTop = Utils.isArriveTop(recyclerView)
                        if (arriveTop && arriveBottom) {
                            LogUtils.d(TAG, "onScrollStateChanged: 已经展示所有数据")
                            return
                        }

                        if (!mAdapter.isEmpty && arriveBottom) {
                            mPresenter.loadMoreArchitectureContent(mBean.id)
                        }
                    }
                }
            })
        }

        ArticleDetailHelper.instance.getArticleUpdateDetail().observe(viewLifecycleOwner, Observer { bean: ArticleDetailBean? ->
            if (bean == null || bean.source != ArticleDetailBean.Source.KNOWLEDGE) return@Observer
            for (data in mAdapter.srcDataList) {
                if (data.id.toLong() == bean.articleId) {
                    data.collect = bean.isCollected
                    mAdapter.refresh(data)
                    return@Observer
                }
            }
        })


        getLoader().showView(ViewType.LOAD);
        mPresenter.refreshArchitectureContent(mBean.id);
    }

    override fun onReloadClick() {
        super.onReloadClick()
        mPresenter.refreshArchitectureContent(mBean.id)
    }

    override fun onRefreshArchitectureFail(code: Int) {
        getLoader().showView(ViewType.ERR);
    }

    override fun onLoadArchitectureContent(content: ArchitectureContentBean, fromRefresh: Boolean) {
        getLoader().hideAll();
        when {
            fromRefresh -> mAdapter.display(content.datas)
            content.over -> showToast(R.string.arrived_end)
            else -> mAdapter.addData(content.datas)
        }
    }

    override fun onArchArticleCollectSuccess(isCollect: Boolean, article: ArchitectureContentBean.Data) {
        article.collect = isCollect;
        mAdapter.refresh(article);
    }

    override fun onArchArticleCollectFail(isCollect: Boolean, article: ArchitectureContentBean.Data, code: Int) {
        showToast(if (isCollect) R.string.collect_fail else R.string.cancel_fail)
    }
}