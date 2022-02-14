package com.ytempest.wanandroid.activity.architecture.content

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ytempest.tool.util.LogUtils
import com.ytempest.wanandroid.R
import com.ytempest.framework.base.createViewModel
import com.ytempest.framework.base.fragment.MVVMFragment
import com.ytempest.wanandroid.base.load.Loader
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.base.vm.EntityObserver
import com.ytempest.wanandroid.databinding.FragArchContentBinding
import com.ytempest.framework.ext.getBundle
import com.ytempest.wanandroid.helper.ArticleDetailHelper
import com.ytempest.wanandroid.http.bean.ArchitectureContentBean
import com.ytempest.wanandroid.http.bean.ArticleDetailBean
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean.Children
import com.ytempest.framework.utils.JSON
import com.ytempest.wanandroid.utils.Utils

/**
 * @author heqidu
 * @since 21-2-22
 */
class ArchArticleFrag() : MVVMFragment<FragArchContentBinding>(), IArchArticleView {

    override val viewModel by lazy { createViewModel<ArchArticleViewModel>() }

    private val loader: Loader by lazy {
        Loader(binding.root as ViewGroup).also {
            it.setReloadCall {
                it.showView(ViewType.LOAD)
                viewModel.refreshArchitectureContent(mBean.id)
            }
        }
    }

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

        mAdapter = ArchArticleAdapter(viewModel)
        with(binding.contentView) {
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
                            if (LogUtils.isLoggable()) LogUtils.d(TAG, "onScrollStateChanged: 已经展示所有数据")
                            return
                        }

                        if (!mAdapter.isEmpty && arriveBottom) {
                            viewModel.loadMoreArchitectureContent(mBean.id)
                        }
                    }
                }
            })
        }

        ArticleDetailHelper.instance.getArticleUpdateDetail().observe(viewLifecycleOwner, Observer { bean: ArticleDetailBean? ->
            if (bean == null || bean.source != ArticleDetailBean.Source.KNOWLEDGE) return@Observer
            for (data in mAdapter.srcDataList) {
                if (data.id == bean.articleId) {
                    data.collect = bean.isCollected
                    mAdapter.refresh(data)
                    return@Observer
                }
            }
        })


        loader.showView(ViewType.LOAD);
        viewModel.refreshArchitectureContent(mBean.id);

        initData()
    }

    private fun initData() {
        viewModel.architectureContentResult.observe(this, EntityObserver(
                onSuccess = { entity ->
                    onLoadArchitectureContent(entity.data, true)
                },
                onFail = { entity ->
                    onRefreshArchitectureFail(entity.code)
                }
        ))
        viewModel.moreArchitectureContentResult.observe(this, EntityObserver(
                onSuccess = { entity ->
                    onLoadArchitectureContent(entity.data, false)
                },
                onFail = { entity ->
                    onRefreshArchitectureFail(entity.code)
                }
        ))
        viewModel.archArticleCollectResult.observe(this, EntityObserver(
                onSuccess = { entity ->
                    onArchArticleCollectSuccess(entity.data)
                },
                onFail = { entity ->
                    onArchArticleCollectFail(entity.extra as ArchitectureContentBean.Data, entity.code)
                }
        ))
    }

    override fun onRefreshArchitectureFail(code: Int) {
        loader.showView(ViewType.ERR);
    }

    override fun onLoadArchitectureContent(content: ArchitectureContentBean, fromRefresh: Boolean) {
        loader.hideAll();
        when {
            fromRefresh -> mAdapter.display(content.datas)
            content.over -> showToast(R.string.arrived_end)
            else -> mAdapter.addData(content.datas)
        }
    }

    override fun onArchArticleCollectSuccess(article: ArchitectureContentBean.Data) {
        mAdapter.refresh(article);
    }

    override fun onArchArticleCollectFail(article: ArchitectureContentBean.Data, code: Int) {
        showToast(if (article.collect) R.string.collect_fail else R.string.cancel_fail)
    }
}