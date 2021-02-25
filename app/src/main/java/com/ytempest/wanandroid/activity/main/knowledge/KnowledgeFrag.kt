package com.ytempest.wanandroid.activity.main.knowledge

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ytempest.wanandroid.base.fragment.LoaderFrag
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.layoutinjector.annotation.InjectLayout
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean
import kotlinx.android.synthetic.main.frag_knowledge.*
import java.util.*

/**
 * @author heqidu
 * @since 21-2-9
 */
@InjectLayout(R.layout.frag_knowledge)
class KnowledgeFrag : LoaderFrag<KnowledgePresenter>(), IKnowledgeView {

    private lateinit var mAdapter: ContentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ContentAdapter()
        group_knowledge_list.layoutManager = LinearLayoutManager(context)
        group_knowledge_list.adapter = mAdapter
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