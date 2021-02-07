package com.ytempest.wanandroid.widget.behavior;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * @author ytempest
 * @since 2021/1/26
 */
public abstract class TriggerBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    private final int mTouchSlop;
    private int mPerTotalScroll;
    private AnimateHelper mAnimateHelper;

    public TriggerBehavior(Context context) {
        this(context, null);
    }

    public TriggerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull View dependency) {
        boolean dependent = isDependent(child, dependency);
        if (dependent) {
            if (mAnimateHelper == null) {
                mAnimateHelper = createAnimateHelper(parent, child, dependency);
            }
        }
        return dependent;
    }

    protected boolean isDependent(V child, View dependency) {
        // 通过需要依赖的View解决CoordinatorLayout异常：This graph contains cyclic dependencies
        CoordinatorLayout.LayoutParams dependencyLp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        CoordinatorLayout.Behavior dependencyBehavior = dependencyLp.getBehavior();
        return !(dependencyBehavior instanceof TriggerBehavior);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull V child, int layoutDirection) {
        mAnimateHelper.onViewChanged();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        mPerTotalScroll += dy;
        boolean overSlop = Math.abs(dy) > mTouchSlop || Math.abs(mPerTotalScroll) > mTouchSlop * 2;
        if (dy < 0) {
            if (overSlop || !target.canScrollVertically(-1)) {
                mAnimateHelper.show();
                mPerTotalScroll = 0;
            }

        } else if (dy > 0) {
            if (overSlop || !target.canScrollVertically(1)) {
                mAnimateHelper.hide();
                mPerTotalScroll = 0;
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        mPerTotalScroll = 0;
    }

    protected abstract AnimateHelper createAnimateHelper(CoordinatorLayout parent, V child, View dependency);
}

