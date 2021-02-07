package com.ytempest.wanandroid.widget.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @author ytempest
 * @since 2021/1/26
 */
public class TranslateAnimateHelper implements AnimateHelper {
    public static final int SLIDE_TO_TOP = 101;
    public static final int SLIDE_TO_BOTTOM = 102;
    public static final int SLIDE_TO_LEFT = 103;
    public static final int SLIDE_TO_RIGHT = 104;

    public static final int IDLE = 1;
    public static final int HIDING = 2;
    public static final int SHOWING = 3;

    private final View mTarget;
    private final int mSlideDirection;
    private final ValueAnimator mAnimator;
    private float mOffset;
    private int mAnimState = IDLE;

    /**
     * @param target    target translate of view
     * @param direction translation direction of the view. Should be {@link #SLIDE_TO_TOP},
     *                  {@link #SLIDE_TO_BOTTOM}, {@link #SLIDE_TO_LEFT} or {@link #SLIDE_TO_RIGHT}
     */
    public TranslateAnimateHelper(View target, int direction) {
        this.mTarget = target;
        this.mSlideDirection = direction;
        updateOffsetVal();

        mAnimator = new ValueAnimator();
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(300);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setCurrentVal((Float) animation.getAnimatedValue());
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimState = IDLE;
            }
        });
    }

    private void updateOffsetVal() {
        final float lastOffset = mOffset;
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mTarget.getLayoutParams();
        final int totalHeight = lp.topMargin + mTarget.getMeasuredHeight() + lp.bottomMargin;
        final int totalWidth = lp.leftMargin + mTarget.getMeasuredWidth() + lp.rightMargin;
        switch (mSlideDirection) {
            case SLIDE_TO_TOP:
                mOffset = -totalHeight;
                break;

            default:
            case SLIDE_TO_BOTTOM:
                mOffset = totalHeight;
                break;

            case SLIDE_TO_LEFT:
                mOffset = -totalWidth;
                break;

            case SLIDE_TO_RIGHT:
                mOffset = totalWidth;
                break;
        }

        if (getCurrentVal() != 0 && lastOffset != mOffset) {
            setCurrentVal(mOffset);
        }
    }

    public TranslateAnimateHelper setInterpolator(TimeInterpolator interpolator) {
        mAnimator.setInterpolator(interpolator);
        return this;
    }

    public TranslateAnimateHelper setDuration(long duration) {
        mAnimator.setDuration(duration);
        return this;
    }

    private void setCurrentVal(float val) {
        if (mSlideDirection == SLIDE_TO_TOP || mSlideDirection == SLIDE_TO_BOTTOM) {
            mTarget.setTranslationY(val);
        } else {
            mTarget.setTranslationX(val);
        }
    }

    private float getCurrentVal() {
        if (mSlideDirection == SLIDE_TO_TOP || mSlideDirection == SLIDE_TO_BOTTOM) {
            return mTarget.getTranslationY();
        } else {
            return mTarget.getTranslationX();
        }
    }

    /**
     * @return current animation status, {@link #IDLE#HIDING#SHOWING}
     */
    public int getAnimState() {
        return mAnimState;
    }

    @Override
    public void onViewChanged() {
        updateOffsetVal();
    }

    @Override
    public void show() {
        final float current = getCurrentVal();
        final float end = 0;
        if (mAnimState == IDLE && current == end) {
            return;
        }
        if (mAnimState != SHOWING) {
            if (mAnimator.isRunning()) {
                mAnimator.cancel();
            }
            mAnimState = SHOWING;
            mAnimator.setFloatValues(current, end);
            mAnimator.start();
        }
    }

    @Override
    public void hide() {
        final float current = getCurrentVal();
        final float end = mOffset;
        if (mAnimState == IDLE && current == end) {
            return;
        }
        if (mAnimState != HIDING) {
            if (mAnimator.isRunning()) {
                mAnimator.cancel();
            }
            mAnimState = HIDING;
            mAnimator.setFloatValues(current, end);
            mAnimator.start();
        }
    }
}
