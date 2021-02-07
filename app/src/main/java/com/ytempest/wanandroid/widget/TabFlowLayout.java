package com.ytempest.wanandroid.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.ytempest.tool.util.DataUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author heqidu
 * @since 2020/12/30
 */
public class TabFlowLayout extends FlowLayout {

    public TabFlowLayout(Context context) {
        this(context, null);
    }

    public TabFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public <Item> void setViewBinder(ViewBinder<Item> binder) {
        if (binder == null || binder.isEmpty()) {
            reset();
            return;
        }
        int childCount = getChildCount();
        int size = binder.getItemCount();
        // 布局已存在的子View数量，根据数据长度，多则移除少则创建
        int diffCount = Math.abs(childCount - size);
        if (childCount > size) {
            removeViews(size, diffCount);

        } else {
            for (int i = 0; i < diffCount; i++) {
                View view = binder.createView(this);
                addView(view);
            }
        }

        for (int i = 0; i < getChildCount(); i++) {
            binder.bindView(this, getChildAt(i), i);
        }
    }

    public static abstract class ViewBinder<Item> {

        private final List<Item> mDataList = new ArrayList<>();
        private LayoutInflater mInflater;

        public ViewBinder(Item... array) {
            if (!DataUtils.isEmpty(array)) {
                Collections.addAll(mDataList, array);
            }
        }

        public ViewBinder(List<Item> list) {
            if (!DataUtils.isEmpty(list)) {
                mDataList.addAll(list);
            }
        }

        public int getItemCount() {
            return mDataList.size();
        }

        public boolean isEmpty() {
            return mDataList.isEmpty();
        }

        View createView(TabFlowLayout layout) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(layout.getContext());
            }
            return onCreateView(mInflater, layout);
        }

        void bindView(TabFlowLayout layout, View view, int index) {
            Item item = mDataList.get(index);
            onBindView(layout, view, item, index);
        }

        @NonNull
        protected abstract View onCreateView(LayoutInflater inflater, TabFlowLayout layout);

        protected abstract void onBindView(TabFlowLayout layout, View view, Item data, int index);
    }
}
