package com.ytempest.tool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ytempest.tool.R;

/**
 * @author ytempest
 * @since 2019/9/7
 */
public class CountView extends LinearLayout {

    public CountView(Context context) {
        this(context, null);
    }

    public CountView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_count_view, this, true);
        initAttrs(context, attrs);
    }

    private TextView mTitleTv;
    private TextView mNumTv;
    private SeekBar mSeekBar;

    private String mTitle;
    private int mMin;
    private int mMax;
    private int mProgress;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitleTv = findViewById(R.id.countView_title);
        mNumTv = findViewById(R.id.countView_num);
        mSeekBar = findViewById(R.id.countView_progress);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateNum();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setTitle(mTitle);
        setMax(mMax - mMin);
        setProgress(mProgress);
        updateNum();
    }

    private void updateNum() {
        mNumTv.setText(String.format(" (%s):", getProgress()));
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CountView);
        mTitle = array.getString(R.styleable.CountView_title);
        mMin = array.getInteger(R.styleable.CountView_min, 0);
        mMax = array.getInteger(R.styleable.CountView_max, 100);
        mProgress = array.getInteger(R.styleable.CountView_current, (mMax + mMin) / 2);
        array.recycle();
    }


    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public String getTitle() {
        return mTitleTv.getText().toString();
    }

    public void setProgress(int progress) {
        mSeekBar.setProgress(progress - mMin);
    }

    public int getProgress() {
        return mSeekBar.getProgress() + mMin;
    }

    public void setMin(int min) {
        mMin = min;
        updateSeekBar();
    }

    public int getMin() {
        return mMin;
    }

    public void setMax(int max) {
        mMax = max;
        updateSeekBar();
    }

    public int getMax() {
        return mMax;
    }

    private void updateSeekBar() {
        mSeekBar.setMax(mMax - mMin);
    }
}
