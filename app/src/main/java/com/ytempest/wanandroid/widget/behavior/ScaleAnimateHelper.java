package com.ytempest.wanandroid.widget.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author ytempest
 * @since 2021/1/26
 */
public class ScaleAnimateHelper implements AnimateHelper {

    private static final int SCALE_LEN = 200;
    private static final int REBOUND_LEN = 300;

    private final View mTarget;
    private final AnimatorSet mHideAnim;
    private final AnimatorSet mShowAnim;
    private boolean isShowed;

    public ScaleAnimateHelper(View target) {
        mTarget = target;
        mHideAnim = new AnimatorSet();
        mHideAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isShowed = false;
            }
        });
        mHideAnim.playTogether(
                ObjectAnimator.ofFloat(mTarget, "scaleX", 1, 0),
                ObjectAnimator.ofFloat(mTarget, "scaleY", 1, 0));
        mHideAnim.setInterpolator(new LinearInterpolator());
        mHideAnim.setDuration(SCALE_LEN);

        mShowAnim = new AnimatorSet();
        mShowAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isShowed = true;
            }
        });

        ObjectAnimator showX = ObjectAnimator.ofFloat(mTarget, "scaleX", 0, 1);
        showX.setDuration(SCALE_LEN);
        ObjectAnimator showY = ObjectAnimator.ofFloat(mTarget, "scaleY", 0, 1);
        showY.setDuration(SCALE_LEN);

        ObjectAnimator rebound = ObjectAnimator.ofFloat(mTarget, "translationY", getReboundArray());
        rebound.setDuration(REBOUND_LEN);
        rebound.setStartDelay(SCALE_LEN);

        mShowAnim.playTogether(showX, showY, rebound);
        mShowAnim.setInterpolator(new LinearInterpolator());

        isShowed = mTarget.getScaleX() > 0 || mTarget.getScaleY() > 0;
    }

    private float[] getReboundArray() {
        float[] res = new float[]{0, -9, 0, -4, 0};
        DisplayMetrics metrics = mTarget.getResources().getDisplayMetrics();
        for (int i = 0; i < res.length; i++) {
            res[i] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, res[i], metrics);
        }
        return res;
    }

    @Override
    public void onViewChanged() {
        mTarget.setPivotX(mTarget.getMeasuredWidth() / 2F);
        mTarget.setPivotY(mTarget.getMeasuredHeight());
    }

    @Override
    public void show() {
        if (isShowed || mHideAnim.isRunning()) {
            return;
        }
        mShowAnim.start();
    }

    @Override
    public void hide() {
        if (!isShowed || mShowAnim.isRunning()) {
            return;
        }
        mHideAnim.start();
    }
}
