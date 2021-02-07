package com.ytempest.wanandroid.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ytempest.tool.util.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heqidu
 * @since 2020/12/25
 */
public abstract class CoreFragPagerAdapter<Item> extends FragmentPagerAdapter {

    private final List<Item> mData = new ArrayList<>();
    private final FragmentManager mFragManager;

    public CoreFragPagerAdapter(FragmentManager fm) {
        this(fm, null);
    }

    public CoreFragPagerAdapter(FragmentManager fm, List<Item> items) {
        super(fm);
        mFragManager = fm;
        if (!DataUtils.isEmpty(items)) {
            mData.addAll(items);
        }
    }

    public FragmentManager getFragManager() {
        return mFragManager;
    }

    public List<Item> getSrcData() {
        return mData;
    }

    public void display(List<Item> data) {
        mData.clear();
        if (!DataUtils.isEmpty(data)) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void add(Item data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    public void add(List<Item> data) {
        if (!DataUtils.isEmpty(data)) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public boolean remove(Item data) {
        boolean remove = mData.remove(data);
        if (remove) {
            notifyDataSetChanged();
        }
        return remove;
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public boolean refresh(Item data) {
        int pos = mData.indexOf(data);
        if (pos >= 0) {
            mData.set(pos, data);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public Fragment getItem(int pos) {
        Item data = mData.get(pos);
        return onCreateFragment(data, pos);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    protected abstract Fragment onCreateFragment(Item data, int pos);
}
