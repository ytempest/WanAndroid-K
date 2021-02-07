package com.ytempest.wanandroid.base.activity;

import android.view.ViewGroup;

import com.ytempest.wanandroid.base.load.Loader;
import com.ytempest.wanandroid.base.presenter.IPresenter;

/**
 * @author ytempest
 * @since 2021/1/6
 */
public abstract class LoaderActivity<Presenter extends IPresenter> extends MvpActivity<Presenter> {
    private static final String TAG = "LoaderActivity";

    private Loader mLoader;

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        ViewGroup root = findViewById(android.R.id.content);
        mLoader = new Loader(root);
        mLoader.setReloadCall(aVoid -> onReloadClick());
    }

    protected void onReloadClick() {

    }

    public Loader getLoader() {
        return mLoader;
    }
}
