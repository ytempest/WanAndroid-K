package com.ytempest.wanandroid.activity.main.project;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ytempest.wanandroid.activity.main.project.content.ProjectContentFrag;
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean;
import com.ytempest.wanandroid.utils.CoreFragPagerAdapter;

/**
 * @author heqidu
 * @since 2020/12/25
 */
public class ProjectClassifyAdapter extends CoreFragPagerAdapter<ProjectClassifyBean> {

    public ProjectClassifyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    protected Fragment onCreateFragment(ProjectClassifyBean data, int pos) {
        return ProjectContentFrag.newInstance(data);
    }

}
