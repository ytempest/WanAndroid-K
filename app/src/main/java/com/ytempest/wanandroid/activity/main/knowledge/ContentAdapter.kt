package com.ytempest.wanandroid.activity.main.knowledge

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ytempest.wanandroid.activity.architecture.ArchitectureActivity
import com.ytempest.tool.adapter.CoreRecyclerAdapter
import com.ytempest.tool.adapter.CoreViewHolder
import com.ytempest.tool.util.RandomUtil
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean
import com.ytempest.wanandroid.widget.TabFlowLayout

/**
 * @author heqidu
 * @since 21-2-9
 */
class ContentAdapter : CoreRecyclerAdapter<KnowledgeArchitectureBean>() {

    private val tabColors: IntArray by lazy { recyclerView.resources.getIntArray(R.array.knowledge_tabs) }

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup, position: Int): CoreViewHolder {
        val view = inflater.inflate(R.layout.item_knowledge_content, viewGroup, false)
        val holder = CoreViewHolder(view)
        holder.isNeedClick = true
        return holder
    }

    override fun onBindData(holder: CoreViewHolder, data: KnowledgeArchitectureBean, position: Int) {
        holder.setText(R.id.tv_item_knowledge_content, data.name)
        val tabLayout = holder.getViewById<TabFlowLayout>(R.id.group_knowledge_content_container)
        tabLayout.setViewBinder(object : TabFlowLayout.ViewBinder<KnowledgeArchitectureBean.Children>(data.children) {

            override fun onCreateView(inflater: LayoutInflater, layout: TabFlowLayout?): View {
                return inflater.inflate(R.layout.item_knowledge_content_tab, layout, false)
            }

            override fun onBindView(layout: TabFlowLayout, view: View, data: KnowledgeArchitectureBean.Children, index: Int) {
                val color = if (tabColors.isEmpty()) Color.GRAY
                else tabColors[RandomUtil.nextInt(tabColors.size)]

                val tabView = view.findViewById<TextView>(R.id.tv_item_knowledge_content_tab)
                tabView.setBackgroundColor(Color.argb(0xDD, Color.red(color), Color.green(color), Color.blue(color)));
                tabView.text = data.name
            }
        })
    }

    override fun onItemClick(holder: CoreViewHolder, view: View, position: Int) {
        super.onItemClick(holder, view, position)
        val data = getData(position)
        ArchitectureActivity.start(view.context, data)
    }
}