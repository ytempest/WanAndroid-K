package com.ytempest.wanandroid.activity.architecture;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ytempest.variety.BaseAdapter;
import com.ytempest.variety.ColorTrackTextView;
import com.ytempest.variety.TabGroup;
import com.ytempest.variety.indicator.DynamicIndicator;
import com.ytempest.variety.indicator.IIndicatorDecorator;
import com.ytempest.variety.tab.ITabDecorator;
import com.ytempest.variety.tab.TrackTabDecorator;
import com.ytempest.wanandroid.R;

import java.util.List;

/**
 * @author heqidu
 * @since 2021/1/14
 */
class TabAdapter extends BaseAdapter<String> {

    public TabAdapter(List<String> list) {
        super(list);
    }

    @Override
    public ITabDecorator<String> getTabDecorator() {
        return new TrackTabDecorator() {
            @Override
            public View createTabView(LayoutInflater inflater, TabGroup parent, int position) {
                ColorTrackTextView view = (ColorTrackTextView) super.createTabView(inflater, parent, position);
                view.setOriginColor(view.getResources().getColor(R.color.common_light_gray_text));
                view.setChangeColor(view.getResources().getColor(R.color.arch_indicator));
                return view;
            }
        };
    }

    @Override
    public IIndicatorDecorator getIndicatorDecorator() {
        return new DynamicIndicator() {
            @Override
            public View createIndicatorView(LayoutInflater inflater, ViewGroup parent) {
                return inflater.inflate(R.layout.indicator_arch_article, parent, false);
            }
        };
    }
}
