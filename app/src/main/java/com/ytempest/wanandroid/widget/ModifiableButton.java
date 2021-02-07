package com.ytempest.wanandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ytempest.wanandroid.R;

/**
 * @author heqidu
 * @since 2020/8/12
 */
public class ModifiableButton extends TextView {

    private int mNormalTextColor;
    private int mDisableTextColor;
    private Drawable mNormalBg;
    private Drawable mDisableBg;

    public ModifiableButton(Context context) {
        this(context, null);
    }

    public ModifiableButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ModifiableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ModifiableButton);
        mDisableTextColor = array.getColor(R.styleable.ModifiableButton_disable_text_color, Color.WHITE);
        mDisableBg = array.getDrawable(R.styleable.ModifiableButton_disable_bg);
        mNormalTextColor = array.getColor(R.styleable.ModifiableButton_normal_text_color, Color.GRAY);
        mNormalBg = array.getDrawable(R.styleable.ModifiableButton_normal_bg);
        array.recycle();
        setDisableStatus();
    }

    public void setNormalStatus() {
        if (!isEnabled()) {
            setEnabled(true);
            setTextColor(mNormalTextColor);
            setBackground(mNormalBg);
        }
    }

    public void setDisableStatus() {
        if (isEnabled()) {
            setEnabled(false);
            setTextColor(mDisableTextColor);
            setBackground(mDisableBg);
        }
    }
}
