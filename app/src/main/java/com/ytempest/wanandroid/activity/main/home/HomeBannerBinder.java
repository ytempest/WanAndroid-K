package com.ytempest.wanandroid.activity.main.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ytempest.banner.AbsBannerBinder;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.http.bean.BannerBean;
import com.ytempest.wanandroid.utils.ImgLoader;

/**
 * @author heqidu
 * @since 2020/11/25
 */
public class HomeBannerBinder extends AbsBannerBinder<BannerBean> implements View.OnClickListener {

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, BannerBean data, int position) {
        View view = inflater.inflate(R.layout.item_banner_content, container, false);
        ImageView imgView = view.findViewById(R.id.iv_item_banner);
        ImgLoader.loadTo(imgView, data.getImagePath());
        view.setTag(data);
        view.setOnClickListener(this);
        return view;
    }

    @Nullable
    @Override
    protected View onCreateTitleView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.item_banner_title, container, false);
    }

    @Override
    protected void onUpdateTitleView(View view, BannerBean data, int position, int count) {
        TextView titleView = view.findViewById(R.id.tv_banner_title_text);
        titleView.setText(data.getTitle());
        ViewGroup group = view.findViewById(R.id.group_item_banner_title);
        for (int i = 0, size = group.getChildCount(); i < size; i++) {
            group.getChildAt(i).setSelected(i == position);
        }
    }

    @Override
    public void onClick(View view) {
        BannerBean data = (BannerBean) view.getTag();
    }
}
