package com.ytempest.wanandroid.base.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ytempest.layoutinjector.LayoutInjector;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author heqidu
 * @since 2020/6/20
 */
public abstract class AbstractActivity extends AppCompatActivity {

    private Unbinder mBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInjector.inject(this);
        mBind = ButterKnife.bind(this);
        onViewCreated();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
    }

    public Context getContext() {
        return this;
    }

    public AppCompatActivity getActivity() {
        return this;
    }

    protected abstract void onViewCreated();
}
