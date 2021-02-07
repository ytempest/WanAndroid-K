package com.ytempest.wanandroid.widget.behavior;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author ytempest
 * @since 2021/1/26
 */
public class TranslateBehavior extends TriggerBehavior<View> {

    public TranslateBehavior(Context context) {
        super(context);
    }

    public TranslateBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected AnimateHelper createAnimateHelper(CoordinatorLayout parent, View child, View dependency) {
        return new TranslateAnimateHelper(child, TranslateAnimateHelper.SLIDE_TO_BOTTOM);
    }
}
