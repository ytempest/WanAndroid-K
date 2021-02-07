package com.ytempest.wanandroid.widget.behavior;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author ytempest
 * @since 2021/1/26
 */
public class ScaleFloatButtonBehavior extends TriggerBehavior<View> {

    public ScaleFloatButtonBehavior(Context context) {
        super(context);
    }

    public ScaleFloatButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected AnimateHelper createAnimateHelper(CoordinatorLayout parent, View child, View dependency) {
        return new ScaleAnimateHelper(child);
    }

}
