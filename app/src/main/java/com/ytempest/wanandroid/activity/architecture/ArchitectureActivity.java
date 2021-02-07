package com.ytempest.wanandroid.activity.architecture;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ytempest.layoutinjector.annotation.InjectLayout;
import com.ytempest.tool.helper.ActivityLauncher;
import com.ytempest.tool.util.DataUtils;
import com.ytempest.tool.util.IntentUtils;
import com.ytempest.tool.util.ToastUtils;
import com.ytempest.variety.VarietyTabLayout;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.activity.architecture.content.ArchArticleFrag;
import com.ytempest.wanandroid.base.activity.LoaderActivity;
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean;
import com.ytempest.wanandroid.utils.CoreFragPagerAdapter;
import com.ytempest.wanandroid.utils.JSON;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author ytempest
 * @since 2021/1/6
 */
@InjectLayout(R.layout.activity_architecture)
public class ArchitectureActivity extends LoaderActivity<ArchitecturePresenter> implements IArchitectureContract.View {

    private static final String TAG = ArchitectureActivity.class.getSimpleName();

    private static final String KEY_ARCHITECTURE_DATA = "architecture_data";

    public static void start(Context context, KnowledgeArchitectureBean bean) {
        Intent intent = new Intent(context, ArchitectureActivity.class);
        intent.putExtra(KEY_ARCHITECTURE_DATA, JSON.toJson(bean));
        ActivityLauncher.startActivity(context, intent);
    }

    @BindView(R.id.group_arch_indicator)
    VarietyTabLayout mTabLayout;
    @BindView(R.id.group_arch_content)
    ViewPager mViewPager;
    private KnowledgeArchitectureBean mBean;

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        String json = IntentUtils.getString(getIntent(), KEY_ARCHITECTURE_DATA, null);
        mBean = JSON.from(json, KnowledgeArchitectureBean.class);
        if (mBean == null) {
            ToastUtils.show(getContext(), R.string.get_data_fail);
            return;
        }
        List<KnowledgeArchitectureBean.ChildrenBean> children = mBean.getChildren();
        List<String> tabs = new ArrayList<>();
        for (int i = 0, len = DataUtils.getSize(children); i < len; i++) {
            tabs.add(children.get(i).getName());
        }

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setAdapter(new TabAdapter(tabs));
        mViewPager.setAdapter(new CoreFragPagerAdapter<KnowledgeArchitectureBean.ChildrenBean>
                (getSupportFragmentManager(), children) {
            @Override
            protected Fragment onCreateFragment(KnowledgeArchitectureBean.ChildrenBean data, int pos) {
                return ArchArticleFrag.newInstance(data);
            }
        });

    }
}