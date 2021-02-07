package com.ytempest.wanandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.ytempest.wanandroid.R;

/**
 * @author heqidu
 * @since 2020/12/21
 */
public class MaskLayout extends FrameLayout {

    private int mMaskLayoutId;
    private View mMaskView;

    public MaskLayout(Context context) {
        this(context, null);
    }

    public MaskLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MaskLayout);
        mMaskLayoutId = array.getResourceId(R.styleable.MaskLayout_mask_layout, -1);
        array.recycle();
        if (mMaskLayoutId == -1) {
            throw new IllegalStateException("Please set the mask layout use app:mask_layout");
        }
    }

    public void showMask() {
        initMaskView();
        if (mMaskView.getVisibility() == View.GONE) {
            mListener.onMaskShow(mMaskView);
            mMaskView.setVisibility(VISIBLE);
        }
    }

    public void hideMask() {
        if (mMaskView != null && mMaskView.getVisibility() == View.VISIBLE) {
            mListener.onMaskHide(mMaskView);
            mMaskView.setVisibility(GONE);
        }
    }

    private void initMaskView() {
        if (mMaskView == null) {
            mMaskView = LayoutInflater.from(getContext()).inflate(mMaskLayoutId, this, false);
            // 覆盖底层View的点击和滚动
            mMaskView.setClickable(true);
            mListener.onMaskCreated(mMaskView);

            addView(mMaskView);
            mMaskView.setVisibility(GONE);
        }
    }

    private SimpleMaskActionListener mListener = new SimpleMaskActionListener();

    public void setMaskActionListener(SimpleMaskActionListener listener) {
        if (mListener != null) {
            mListener = listener;
        }
    }

    public static class SimpleMaskActionListener {
        public void onMaskCreated(View maskView) {
        }

        public void onMaskShow(View maskView) {
        }

        public void onMaskHide(View maskView) {
        }
    }

}

