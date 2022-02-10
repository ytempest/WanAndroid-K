package com.ytempest.wanandroid.activity.main.project.content

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ytempest.tool.util.LogUtils
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.activity.main.project.content.list.ContentListAdapter
import com.ytempest.wanandroid.base.createViewModel
import com.ytempest.wanandroid.base.fragment.MVVMFragment
import com.ytempest.wanandroid.base.load.Loader
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.base.vm.EntityObserver
import com.ytempest.wanandroid.databinding.FragProjectContentBinding
import com.ytempest.wanandroid.ext.getBundle
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean
import com.ytempest.wanandroid.http.bean.ProjectContentBean
import com.ytempest.wanandroid.utils.JSON
import com.ytempest.wanandroid.utils.Utils
import java.util.*

/**
 * @author heqidu
 * @since 21-2-10
 */
class ProjectContentFrag : MVVMFragment<FragProjectContentBinding>(), IProjectContentView {

    private val TAG = "ProjectContentFrag"

    companion object {
        private const val KEY_CLASSIFY_DATA = "classify_data"

        fun newInstance(data: ProjectClassifyBean): ProjectContentFrag {
            val frag = ProjectContentFrag()
            val bundle = frag.getBundle()
            bundle.putString(KEY_CLASSIFY_DATA, JSON.toJson(data))
            return frag
        }
    }

    override val viewModel by lazy { createViewModel<ProjectContentViewModel>() }
    private lateinit var mClassifyBean: ProjectClassifyBean
    private lateinit var mAdapter: ContentListAdapter

    private val loader: Loader by lazy {
        Loader(binding.root as ViewGroup).also {
            it.setReloadCall {
                it.showView(ViewType.LOAD)
                viewModel.refreshContent(mClassifyBean)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mClassifyBean = JSON.from(
            getBundle().getString(KEY_CLASSIFY_DATA),
            ProjectClassifyBean::class.java
        ) as ProjectClassifyBean
        Objects.requireNonNull(mClassifyBean)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ContentListAdapter(viewModel)
        binding.listView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val arriveTop = Utils.isArriveTop(recyclerView)
                        val arriveBottom = Utils.isArriveBottom(recyclerView)
                        if (arriveTop && arriveBottom) {
                            LogUtils.d(TAG, "onScrollStateChanged: 已经展示所有数据")
                            return
                        }
                        if (mAdapter.isEmpty && arriveBottom) {
                            viewModel.loadMoreProjectContent(mClassifyBean)
                        }
                    }
                }
            })
        }

        initData()
    }

    private fun initData() {
        viewModel.projectContentResult.observe(this, EntityObserver(
            onSuccess = { entity ->
                val fromLoadMore = if (entity.extra is Boolean) entity.extra else false
                if (fromLoadMore) {
                    onMoreProjectContentLoaded(entity.data)
                } else {
                    displayProjectContent(entity.data)
                }
            },
            onFail = { entity ->
                val fromLoadMore = if (entity.extra is Boolean) entity.extra else false
                if (fromLoadMore) {
                    // do nothing
                } else {
                    onProjectContentFail(entity.code)
                }
            }
        ))

        viewModel.projectArticleCollectResult.observe(this, EntityObserver(
            onSuccess = { entity ->
                onProjectArticleCollectSuccess(entity.data)
            },
            onFail = { entity ->
                onProjectArticleCollectFail(entity.code, entity.extra as Boolean)
            }
        ))

        loader.showView(ViewType.LOAD)
        viewModel.refreshContent(mClassifyBean)
    }

    override fun displayProjectContent(projectContent: ProjectContentBean) {
        loader.hideAll()
        if (projectContent.over) showToast(R.string.arrived_end)
        else mAdapter.display(projectContent.datas)
    }

    override fun onProjectContentFail(code: Int) {
        if (mAdapter.isEmpty) {
            loader.showView(ViewType.ERR)
        }
    }

    override fun onMoreProjectContentLoaded(projectContent: ProjectContentBean) {
        if (projectContent.over) showToast(R.string.arrived_end)
        else mAdapter.addData(projectContent.datas)
    }

    override fun onProjectArticleCollectSuccess(bean: ProjectContentBean.Data) =
        showToast(R.string.collect_success)

    override fun onProjectArticleCollectFail(code: Int, onceCollected: Boolean) =
        showToast(if (onceCollected) R.string.once_collected else R.string.collect_fail)
}