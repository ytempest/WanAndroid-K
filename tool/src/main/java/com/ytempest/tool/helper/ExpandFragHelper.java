package com.ytempest.tool.helper;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author heqidu
 * @since 2020/6/24
 */
public class ExpandFragHelper {


    private final FragmentManager mManager;
    private final FragConstructor mFragConstructor;

    public ExpandFragHelper(FragmentManager manager, FragConstructor fragConstructor) {
        mManager = manager;
        mFragConstructor = fragConstructor;
    }

    public FragmentManager getManager() {
        return mManager;
    }

    @Nullable
    public Fragment findFrag(int fragId) {
        return mManager.findFragmentByTag(caleTag(fragId));
    }

    @Nullable
    public Fragment removeFrag(int fragId) {
        Fragment fragment = mManager.findFragmentByTag(caleTag(fragId));
        if (fragment != null) {
            FragmentTransaction transaction = mManager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
        return fragment;
    }

    public Fragment switchFrag(@IdRes int containerId, @Nullable Fragment curFrag, int nextFragId) {
        String tag = caleTag(nextFragId);
        Fragment nextFrag = mManager.findFragmentByTag(tag);
        if (nextFrag == null) {
            nextFrag = mFragConstructor.createFrag(nextFragId);
            if (nextFrag == null) {
                throw new IllegalArgumentException("Not found the fragment instance by id: " + nextFragId);
            }
        }

        if (curFrag != null && curFrag.getClass() == nextFrag.getClass()) {
            return curFrag;
        }

        FragmentTransaction transaction = mManager.beginTransaction();
        if (curFrag != null) {
            transaction.hide(curFrag);
        }

        if (nextFrag.isAdded()) {
            transaction.show(nextFrag);
        } else {
            transaction.add(containerId, nextFrag, tag);
        }
        transaction.commit();
        return nextFrag;
    }


    private static final String KEY_PREFIX = "frag-";

    private String caleTag(int fragId) {
        return KEY_PREFIX + fragId;
    }

    public interface FragConstructor {
        Fragment createFrag(int fragId);
    }
}
