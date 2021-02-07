package com.ytempest.wanandroid.activity.main.knowledge;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ytempest.tool.adapter.CoreRecyclerAdapter;
import com.ytempest.tool.adapter.CoreViewHolder;
import com.ytempest.tool.util.RandomUtil;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.activity.architecture.ArchitectureActivity;
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean;
import com.ytempest.wanandroid.widget.TabFlowLayout;

/**
 * @author heqidu
 * @since 2021/1/5
 */
class ContentAdapter extends CoreRecyclerAdapter<KnowledgeArchitectureBean> {

    private int[] tabColors;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        tabColors = recyclerView.getResources().getIntArray(R.array.knowledge_tabs);
    }

    @Override
    protected CoreViewHolder onCreateView(LayoutInflater inflater, ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.item_knowledge_content, viewGroup, false);
        CoreViewHolder holder = new CoreViewHolder(view);
        holder.setNeedClick(true);
        return holder;
    }

    @Override
    protected void onBindData(CoreViewHolder holder, KnowledgeArchitectureBean data, int position) {
        holder.setText(R.id.tv_item_knowledge_content, data.getName());
        TabFlowLayout tabLayout = holder.getViewById(R.id.group_knowledge_content_container);
        tabLayout.setViewBinder(new TabFlowLayout.ViewBinder<KnowledgeArchitectureBean.ChildrenBean>(data.getChildren()) {
            @NonNull
            @Override
            protected View onCreateView(LayoutInflater inflater, TabFlowLayout layout) {
                return inflater.inflate(R.layout.item_knowledge_content_tab, layout, false);
            }

            @Override
            protected void onBindView(TabFlowLayout layout, View view, KnowledgeArchitectureBean.ChildrenBean data, int index) {
                TextView tabView = view.findViewById(R.id.tv_item_knowledge_content_tab);
                int color;
                if (tabColors != null && tabColors.length > 0) {
                    int random = RandomUtil.nextInt(tabColors.length);
                    color = tabColors[random];
                } else {
                    color = Color.GRAY;
                }
                tabView.setBackgroundColor(Color.argb(0xDD, Color.red(color), Color.green(color), Color.blue(color)));
                tabView.setText(data.getName());
            }
        });
    }

    @Override
    protected void onItemClick(CoreViewHolder holder, View view, int position) {
        super.onItemClick(holder, view, position);
        KnowledgeArchitectureBean data = getData(position);
        ArchitectureActivity.start(view.getContext(), data);
    }
}
