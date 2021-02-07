package com.ytempest.wanandroid.base.activity;

import android.os.Bundle;

import com.ytempest.tool.util.ToastUtils;
import com.ytempest.wanandroid.base.presenter.IPresenter;
import com.ytempest.wanandroid.base.view.IView;
import com.ytempest.wanandroid.dialog.LoadingDialog;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


/**
 * @author heqidu
 * @since 2020/6/28
 */
public abstract class MvpActivity<Presenter extends IPresenter> extends AbstractActivity
        implements HasSupportFragmentInjector, IView {

    @Inject
    protected Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewCreated() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }

    /*View*/

    @Override
    public void showToast(String msg) {
        ToastUtils.show(this, msg);
    }

    @Override
    public void showToast(@StringRes int textId) {
        ToastUtils.show(this, textId);
    }

    private LoadingDialog mLoadingDialog;

    @Override
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setAutoDismiss(getLifecycle());
        }
        mLoadingDialog.show();
    }

    @Override
    public void stopLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    // TODO  heqidu: 在Dagger2.20版本上会出现Cannot inject members into raw type com.ytempest.wanandroid.base.activity.MvpActivity
    // TODO  heqidu: 导致无法通过编译，但在Dagger2.15版本上暂时没有问题
    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentDispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentDispatchingAndroidInjector;
    }

}
