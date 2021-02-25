package com.ytempest.wanandroid.activity.main.knowledge

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ytempest.wanandroid.base.fragment.LoaderFrag
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.databinding.FragKnowledgeBinding
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean
import java.util.*

/**
 * @author heqidu
 * @since 21-2-9
 */
class KnowledgeFrag : LoaderFrag<KnowledgePresenter, FragKnowledgeBinding>(), IKnowledgeView {

    private lateinit var mAdapter: ContentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ContentAdapter()
        binding.knowledgeListView.layoutManager = LinearLayoutManager(context)
        binding.knowledgeListView.adapter = mAdapter
        loadKnowledgeArchitecture()
    }

    private fun loadKnowledgeArchitecture() {
        getLoader().showView(ViewType.LOAD)
        mPresenter.getKnowledgeArchitecture()
    }

    override fun onReloadClick() {
        super.onReloadClick()
        loadKnowledgeArchitecture()
    }

    override fun onKnowledgeArchitectureReceived(list: List<KnowledgeArchitectureBean>) {
        getLoader().hideAll()
        Collections.sort(list) { p0, p1 -> p0.order.compareTo(p1.order) }
        mAdapter.display(list)
    }

    override fun onKnowledgeArchitectureFail(code: Int) {
        getLoader().showView(ViewType.ERR);
    }
}