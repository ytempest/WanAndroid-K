package com.ytempest.wanandroid.activity.main.knowledge;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.ytempest.layoutinjector.annotation.InjectLayout;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.base.fragment.LoaderFrag;
import com.ytempest.wanandroid.base.load.ViewType;
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * @author heqidu
 * @since 2020/6/23
 */
@InjectLayout(R.layout.frag_knowledge)
public class KnowledgeFrag extends LoaderFrag<KnowledgePresenter> implements IKnowledgeContract.View {

    private static final String TAG = KnowledgeFrag.class.getSimpleName();

    @BindView(R.id.group_knowledge_list)
    RecyclerView mRecyclerView;
    private ContentAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ContentAdapter();
        mRecyclerView.setAdapter(mAdapter);
        loadKnowledgeArchitecture();
    }

    private void loadKnowledgeArchitecture() {
        getLoader().showView(ViewType.LOAD);
        mPresenter.getKnowledgeArchitecture();
    }

    @Override
    protected void onReloadClick() {
        super.onReloadClick();
        loadKnowledgeArchitecture();
    }

    @Override
    public void onKnowledgeArchitectureReceived(List<KnowledgeArchitectureBean> list) {
        getLoader().hideAll();
        Collections.sort(list, (o1, o2) -> Integer.compare(o1.getOrder(), o2.getOrder()));
        mAdapter.display(list);
    }

    @Override
    public void onKnowledgeArchitectureFail(int code) {
        getLoader().showView(ViewType.ERR);
    }
}
