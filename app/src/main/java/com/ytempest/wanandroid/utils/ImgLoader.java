package com.ytempest.wanandroid.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.ytempest.wanandroid.R;

/**
 * @author heqidu
 * @since 2020/12/15
 */
public class ImgLoader {

    static {
        // 修正Glide引起的 You must not call setTag() on a view Glide is targeting
        ViewTarget.setTagId(R.id.glideIndexTag);
    }

    public static void loadTo(ImageView view, String url) {
        Glide.with(view).load(url).into(view);
    }
}
