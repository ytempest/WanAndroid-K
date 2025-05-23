package com.ytempest.tool.helper;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author heqidu
 * @since 2020/6/23
 */
public class FragHelper {

    private final FragmentManager mManager;

    public FragHelper(FragmentManager manager) {
        mManager = manager;
    }

    public FragmentManager getManager() {
        return mManager;
    }

    @Nullable
    public Fragment findFrag(Class<? extends Fragment> clazz) {
        return clazz != null ? mManager.findFragmentByTag(caleTag(clazz)) : null;
    }

    @Nullable
    public Fragment removeFrag(Class<? extends Fragment> clazz) {
        String tag = caleTag(clazz);
        Fragment fragment = mManager.findFragmentByTag(tag);
        if (fragment != null) {
            FragmentTransaction transaction = mManager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
        return fragment;
    }

    public Fragment switchFrag(@IdRes int containerId, @Nullable Fragment curFrag, @NonNull Fragment nextFrag) {
        return switchFrag(containerId, curFrag, nextFrag, null);
    }

    public Fragment switchFrag(@IdRes int containerId, @Nullable Fragment curFrag, @NonNull Class<? extends Fragment> nextFragClz) {
        return switchFrag(containerId, curFrag, null, nextFragClz);
    }

    private Fragment switchFrag(@IdRes int containerId, @Nullable Fragment curFrag, Fragment nextFrag, Class<? extends Fragment> nextFragClz) {
        if (nextFrag != null) {
            nextFragClz = nextFrag.getClass();
        }

        if (curFrag != null && curFrag.getClass() == nextFragClz) {
            return curFrag;
        }

        String nextFragTag = caleTag(nextFragClz);
        nextFrag = nextFrag != null ? nextFrag : mManager.findFragmentByTag(nextFragTag);
        if (nextFrag == null) {
            nextFrag = newFragment(nextFragClz);
        }

        FragmentTransaction transaction = mManager.beginTransaction();
        if (curFrag != null) {
            transaction.hide(curFrag);
        }

        if (nextFrag.isAdded()) {
            transaction.show(nextFrag);
        } else {
            transaction.add(containerId, nextFrag, nextFragTag);
        }
        transaction.commit();
        return nextFrag;
    }

    private static final String KEY_PREFIX = "frag-";

    private String caleTag(Class<? extends Fragment> clazz) {
        // 这里最好用类的全路径名，若用类名可能会在混淆时出现问题
        return KEY_PREFIX + clazz.getCanonicalName();
    }


    private Fragment newFragment(Class<? extends Fragment> nextFragClz) {
        try {
            return nextFragClz.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Please provide an constructor with no params for " + nextFragClz.getCanonicalName());
        }
    }
}
