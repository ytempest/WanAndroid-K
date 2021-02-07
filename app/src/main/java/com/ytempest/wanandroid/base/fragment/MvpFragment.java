package com.ytempest.wanandroid.base.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.view.View;

import com.ytempest.tool.util.ToastUtils;
import com.ytempest.wanandroid.base.presenter.IPresenter;
import com.ytempest.wanandroid.base.view.IView;
import com.ytempest.wanandroid.dialog.LoadingDialog;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * @author heqidu
 * @since 2020/6/30
 */
public abstract class MvpFragment<Presenter extends IPresenter> extends AbstractFragment implements IView {

    @Inject
    protected Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroyView();
    }


    /*View*/

    @Override
    public void showToast(String msg) {
        ToastUtils.show(getContext(), msg);
    }

    @Override
    public void showToast(@StringRes int textId) {
        ToastUtils.show(getContext(), textId);
    }

    private LoadingDialog mLoadingDialog;

    @Override
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getContext());
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
}
