package com.ytempest.wanandroid.widget.jumping;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.ytempest.wanandroid.R;

public class JumpingView extends LinearLayout {

    private static final int DEF_JUMP_HEIGHT = 70;
    private static final int DEF_SHAPE_SIZE = 30;
    private static final int SHADOW_HEIGHT = 3;

    private static final int ANIMATOR_TIME = 400;
    private static final float SHADOW_SCALE = 0.4f;

    private ShapeView mShapeView;
    private View mShadowView;
    private int mJumpDuration;
    private int mJumpHeight;
    private int mShapeSize;
    private boolean isCancel = true;

    public JumpingView(Context context) {
        this(context, null);
    }

    public JumpingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JumpingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.JumpingView);
        mJumpHeight = (int) array.getDimension(R.styleable.JumpingView_jump_height, dpToPx(DEF_JUMP_HEIGHT));
        mShapeSize = (int) array.getDimension(R.styleable.JumpingView_shape_size, dpToPx(DEF_SHAPE_SIZE));
        mJumpDuration = array.getInteger(R.styleable.JumpingView_jump_duration, -1);
        if (mJumpDuration < 0) {
            mJumpDuration = ANIMATOR_TIME;
        }
        array.recycle();

        initView();
    }

    private void initView() {
        mShapeView = new ShapeView(getContext());
        LayoutParams params = new LayoutParams(mShapeSize, mShapeSize);
        int margin = (int) ((Math.sqrt(mShapeSize * mShapeSize * 2) - mShapeSize) / 2);
        params.setMargins(margin, margin, margin, mJumpHeight);
        addView(mShapeView, params);

        mShadowView = new View(getContext());
        mShadowView.setBackgroundResource(R.drawable.shadow_jumping_view);
        addView(mShadowView, new LayoutParams((int) (mShapeSize * 1.1F), dpToPx(SHADOW_HEIGHT)));
    }

    public void start() {
        if (isCancel) {
            isCancel = false;
            setVisibility(VISIBLE);
            startAnimationInternal();
        }
    }

    public void cancel() {
        if (!isCancel) {
            isCancel = true;
            setVisibility(GONE);
            if (mAnimSet != null) {
                mAnimSet.cancel();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancel();
    }

    private AnimatorSet mAnimSet;

    private void startAnimationInternal() {
        if (mAnimSet == null) {
            mAnimSet = new AnimatorSet();

            AnimatorSet fallAnimation = createFallAnimation();
            AnimatorSet upAnimation = createUpAnimation();
            ObjectAnimator rotation = createRotationAnimation();

            mAnimSet.play(upAnimation)
                    .with(rotation)
                    .after(fallAnimation);
            mAnimSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (!isCancel) {
                        startAnimationInternal();
                    }
                }
            });
        }
        mAnimSet.start();
    }


    private ObjectAnimator createRotationAnimation() {
        final ObjectAnimator rotation = ObjectAnimator.ofFloat(mShapeView, "rotation", 0);
        rotation.setInterpolator(new DecelerateInterpolator());
        rotation.setDuration(mJumpDuration);
        rotation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mShapeView.switchNextShape();
                switch (mShapeView.getShapeId()) {
                    case ShapeView.SHAPE_SQUARE:
                        mShapeView.setPivotX(mShapeView.getMeasuredWidth() / 2F);
                        mShapeView.setPivotY(mShapeView.getMeasuredHeight() / 2F);
                        rotation.setFloatValues(0, 180F);
                        break;

                    case ShapeView.SHAPE_TRIANGLE:
                        mShapeView.setPivotX(mShapeView.getMeasuredWidth() / 2F);
                        mShapeView.setPivotY(mShapeView.getMeasuredHeight() * 0.64F);
                        rotation.setFloatValues(0, -120F);
                        break;

                    case ShapeView.SHAPE_CIRCLE:
                    default:
                        break;
                }
            }
        });
        return rotation;
    }

    private AnimatorSet createUpAnimation() {
        AnimatorSet animator = new AnimatorSet();
        animator.playTogether(
                ObjectAnimator.ofFloat(mShapeView, "translationY", mJumpHeight, 0),
                ObjectAnimator.ofFloat(mShadowView, "scaleX", SHADOW_SCALE, 1F),
                ObjectAnimator.ofFloat(mShadowView, "scaleY", SHADOW_SCALE, 1F)
        );
        animator.setDuration(mJumpDuration);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }

    private AnimatorSet createFallAnimation() {
        AnimatorSet animator = new AnimatorSet();
        animator.playTogether(
                ObjectAnimator.ofFloat(mShapeView, "translationY", 0, mJumpHeight),
                ObjectAnimator.ofFloat(mShadowView, "scaleX", 1F, SHADOW_SCALE),
                ObjectAnimator.ofFloat(mShadowView, "scaleY", 1F, SHADOW_SCALE)
        );
        animator.setDuration(mJumpDuration);
        animator.setInterpolator(new AccelerateInterpolator());
        return animator;
    }

    private int dpToPx(int dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }
}
