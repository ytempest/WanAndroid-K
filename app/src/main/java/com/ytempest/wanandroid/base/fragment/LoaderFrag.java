package com.ytempest.wanandroid.base.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.ytempest.wanandroid.base.load.Loader;
import com.ytempest.wanandroid.base.presenter.IPresenter;

/**
 * @author heqidu
 * @since 2020/12/28
 */
public class LoaderFrag<Presenter extends IPresenter> extends MvpFragment<Presenter> {

    private static final String TAG = LoaderFrag.class.getSimpleName();

    private Loader mLoader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoader = new Loader((ViewGroup) view);
        mLoader.setReloadCall(aVoid -> onReloadClick());
    }

    protected void onReloadClick() {

    }

    public Loader getLoader() {
        return mLoader;
    }
}
