package com.ytempest.wanandroid.activity.main.knowledge

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ytempest.wanandroid.base.createViewModel
import com.ytempest.wanandroid.base.fragment.MVVMFragment
import com.ytempest.wanandroid.base.load.Loader
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.base.vm.EntityObserver
import com.ytempest.wanandroid.databinding.FragKnowledgeBinding
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean
import java.util.*

/**
 * @author heqidu
 * @since 21-2-9
 */
class KnowledgeFrag : MVVMFragment<FragKnowledgeBinding>(), IKnowledgeView {

    override val viewModel by lazy { createViewModel<KnowledgeViewModel>() }
    private lateinit var mAdapter: ContentAdapter

    private val loader: Loader by lazy {
        Loader(binding.root as ViewGroup).also {
            it.setReloadCall {
                it.showView(ViewType.LOAD)
                viewModel.loadKnowledgeArchitecture()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ContentAdapter()
        binding.knowledgeListView.layoutManager = LinearLayoutManager(context)
        binding.knowledgeListView.adapter = mAdapter

        initData()
    }

    private fun initData() {

        viewModel.knowledgeListResult.observe(this, EntityObserver(
            onSuccess = { entity ->
                onKnowledgeArchitectureReceived(entity.data)
            },
            onFail = { entity ->
                onKnowledgeArchitectureFail(entity.code)
            }
        ))

        loader.showView(ViewType.LOAD)
        viewModel.loadKnowledgeArchitecture()
    }

    override fun onKnowledgeArchitectureReceived(list: List<KnowledgeArchitectureBean>) {
        loader.hideAll()
        Collections.sort(list) { p0, p1 -> p0.order.compareTo(p1.order) }
        mAdapter.display(list)
    }

    override fun onKnowledgeArchitectureFail(code: Int) {
        loader.showView(ViewType.ERR);
    }
}