package com.ytempest.wanandroid.activity.main.project.content

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ytempest.layoutinjector.annotation.InjectLayout
import com.ytempest.tool.util.LogUtils
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.activity.main.project.content.list.ContentListAdapter
import com.ytempest.wanandroid.base.fragment.LoaderFrag
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.ext.getBundle
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean
import com.ytempest.wanandroid.http.bean.ProjectContentBean
import com.ytempest.wanandroid.utils.JSON
import com.ytempest.wanandroid.utils.Utils
import kotlinx.android.synthetic.main.frag_project_content.*
import java.util.*

/**
 * @author heqidu
 * @since 21-2-10
 */
@InjectLayout(R.layout.frag_project_content)
class ProjectContentFrag : LoaderFrag<ProjectContentPresenter>(), IProjectContentView {

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

    private lateinit var mClassifyBean: ProjectClassifyBean
    private lateinit var mAdapter: ContentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mClassifyBean = JSON.from(getBundle().getString(KEY_CLASSIFY_DATA), ProjectClassifyBean::class.java) as ProjectClassifyBean
        Objects.requireNonNull(mClassifyBean)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ContentListAdapter(mPresenter)
        with(group_project_content_list) {
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
                            mPresenter.loadMoreProjectContent(mClassifyBean)
                        }
                    }
                }
            })
            getLoader().showView(ViewType.LOAD)
            mPresenter.refreshContent(mClassifyBean)
        }
    }

    override fun onReloadClick() {
        super.onReloadClick()
        mPresenter.refreshContent(mClassifyBean)
    }

    override fun displayProjectContent(projectContent: ProjectContentBean) {
        getLoader().hideAll()
        if (projectContent.over) showToast(R.string.arrived_end)
        else mAdapter.display(projectContent.datas)
    }

    override fun onProjectContentFail(code: Int) {
        if (mAdapter.isEmpty) {
            getLoader().showView(com.ytempest.wanandroid.base.load.ViewType.ERR)
        }
    }

    override fun onMoreProjectContentLoaded(projectContent: ProjectContentBean) {
        if (projectContent.over) showToast(R.string.arrived_end)
        else mAdapter.addData(projectContent.datas)
    }

    override fun onProjectArticleCollectSuccess(bean: ProjectContentBean.Data) =
            showToast(R.string.collect_success)

    override fun onProjectArticleCollectFail(code: Int, onceCollected: Boolean, bean: ProjectContentBean.Data) =
            showToast(if (onceCollected) R.string.once_collected else R.string.collect_fail)
}