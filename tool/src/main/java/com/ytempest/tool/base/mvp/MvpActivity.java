package com.ytempest.tool.base.mvp;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ytempest.tool.base.mvp.inject.InjectPresenter;
import com.ytempest.tool.base.mvp.inject.InjectUtil;


/**
 * @author heqidu
 * @since 2020/6/28
 */
public class MvpActivity<Presenter extends IPresenter> extends AppCompatActivity implements IView, IContract {

    protected Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = InjectUtil.findPresenter(getClass());
        if (mPresenter == null) {
            throw new IllegalArgumentException("Please inject presenter by " + InjectPresenter.class.getCanonicalName());
        }
        // 添加协议
        mPresenter.setContract(this);
        // 添加View
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detach();
    }

    /*Contract*/

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Application getApp() {
        return getApplication();
    }

    /*View*/

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showToast(int textId) {

    }
}
