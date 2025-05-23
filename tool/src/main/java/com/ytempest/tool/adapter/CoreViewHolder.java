package com.ytempest.tool.adapter;

import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author heqidu
 * @since 2020/8/1
 */
public class CoreViewHolder extends RecyclerView.ViewHolder {

    private Object mTag;

    public CoreViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setTag(Object tag) {
        mTag = tag;
    }

    public Object getTag() {
        return mTag;
    }

    public <V extends View> V getRootView() {
        return (V) itemView;
    }

    public <V extends View> V getViewById(@IdRes int viewId) {
        return itemView.findViewById(viewId);
    }

    public Context getContext() {
        return itemView.getContext();
    }

    public void setImageResource(@IdRes int viewId, @DrawableRes int iconId) {
        ImageView imgView = getViewById(viewId);
        imgView.setImageResource(iconId);
    }

    public void setText(@IdRes int viewId, @StringRes int textId) {
        TextView textView = getViewById(viewId);
        textView.setText(textId);
    }

    public void setText(@IdRes int viewId, String text) {
        TextView textView = getViewById(viewId);
        textView.setText(text);
    }

    private boolean needClick;
    private boolean needLongClick;

    public boolean isNeedClick() {
        return needClick;
    }

    public void setNeedClick(boolean needClick) {
        this.needClick = needClick;
    }

    public boolean isNeedLongClick() {
        return needLongClick;
    }

    public void setNeedLongClick(boolean needLongClick) {
        this.needLongClick = needLongClick;
    }
}
