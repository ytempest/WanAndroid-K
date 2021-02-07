package com.ytempest.wanandroid.activity.main.navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.http.bean.NavigationListBean;
import com.ytempest.wanandroid.widget.VerticalTabLayout;

import java.util.List;

/**
 * @author heqidu
 * @since 2021/1/4
 */
public class TitleAdapter extends VerticalTabLayout.Adapter<NavigationListBean> {

    public TitleAdapter(List<NavigationListBean> list) {
        super(list);
    }

    @Override
    protected View createTabView(ViewGroup parent, LayoutInflater inflater, NavigationListBean bean, int position) {
        View view = inflater.inflate(R.layout.item_navigation_title, parent, false);
        view.<TextView>findViewById(R.id.tv_item_navigation_title).setText(bean.getName());
        return view;
    }
}
