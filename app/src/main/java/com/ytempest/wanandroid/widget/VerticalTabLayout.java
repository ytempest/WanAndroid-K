package com.ytempest.wanandroid.widget;

import android.content.Context;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heqidu
 * @since 2021/1/4
 */
public class VerticalTabLayout extends NestedScrollView {

    public VerticalTabLayout(Context context) {
        this(context, null);
    }

    public VerticalTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private final int[] mLocation = new int[2];
    private LinearLayout mTabGroup;
    private int mSelectPosition = -1;
    private Adapter mAdapter;
    private final View.OnClickListener mTabClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            performTabSelect(view);
            final List<TabActonListener> listeners = mTabActonListeners;
            if (listeners != null) {
                int pos = mTabGroup.indexOfChild(view);
                for (TabActonListener listener : listeners) {
                    listener.onTabClick(view, pos);
                }
            }
        }
    };
    private List<TabActonListener> mTabActonListeners;


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTabGroup = new LinearLayout(getContext());
        mTabGroup.setOrientation(LinearLayout.VERTICAL);
        addView(mTabGroup, new NestedScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            mAdapter = adapter;
            mTabGroup.removeAllViews();
            mSelectPosition = -1;

            List list = mAdapter.getData();
            if (!list.isEmpty()) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                for (int i = 0; i < list.size(); i++) {
                    View view = mAdapter.createTabView(mTabGroup, inflater, list.get(i), i);
                    view.setOnClickListener(mTabClickListener);
                    mTabGroup.addView(view);
                }
                performTabSelect(mTabGroup.getChildAt(0));
            }
        }
    }

    /**
     * @return return the position of selected tab, or -1 if this view without tab
     */
    public int getSelectPosition() {
        return mSelectPosition;
    }

    public void smoothScrollToPosition(int position) {
        smoothScrollToPosition(position, true);
    }

    public void smoothScrollToPosition(int position, boolean smoothScroll) {
        View view = mTabGroup.getChildAt(position);
        if (view != null) {
            view.getLocationOnScreen(mLocation);
            int parentH = getHeight();
            int viewY = mLocation[1];
            if (viewY <= 0 || parentH <= viewY) {
                int x = (int) view.getX();
                int y = (int) view.getY();
                if (smoothScroll) {
                    smoothScrollTo(x, y);
                } else {
                    scrollTo(x, y);
                }
            }
            performTabSelect(mTabGroup.getChildAt(position));
        }
    }

    private void performTabSelect(View view) {
        if (mTabActonListeners != null) {
            final int lastPosition = mSelectPosition;
            View lastView = mTabGroup.getChildAt(lastPosition);
            if (lastView != null) {
                for (TabActonListener listener : mTabActonListeners) {
                    listener.onTabUnselected(lastView, mSelectPosition);
                }
            }

            mSelectPosition = mTabGroup.indexOfChild(view);
            for (TabActonListener listener : mTabActonListeners) {
                listener.onTabSelected(view, mSelectPosition);
            }
        }
    }

    public void addTabActonListener(TabActonListener listener) {
        if (listener == null) return;
        if (mTabActonListeners == null) {
            mTabActonListeners = new ArrayList<>();
        }
        mTabActonListeners.add(listener);
    }

    public void removeTabActonListener(TabActonListener listener) {
        if (mTabActonListeners != null) {
            mTabActonListeners.remove(listener);
        }
    }

    public static abstract class Adapter<Item> {

        private final List<Item> mItems = new ArrayList<>();

        public Adapter(List<Item> list) {
            mItems.addAll(list);
        }

        public List<Item> getData() {
            return mItems;
        }

        protected abstract View createTabView(ViewGroup parent, LayoutInflater inflater, Item item, int position);
    }

    public interface TabActonListener {
        void onTabClick(View view, int position);

        void onTabSelected(View view, int position);

        void onTabUnselected(View view, int position);
    }

}
