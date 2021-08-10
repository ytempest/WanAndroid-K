package com.ytempest.wanandroid.base.activity

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ytempest.tool.util.ToastUtils
import com.ytempest.wanandroid.base.presenter.IPresenter
import com.ytempest.wanandroid.base.view.IView
import com.ytempest.wanandroid.dialog.LoadingDialog
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-7
 */
abstract class MvpActivity<Presenter : IPresenter, VB : ViewBinding>
    : AbstractActivity<VB>(), HasSupportFragmentInjector {

    @Inject
    protected lateinit var mPresenter: Presenter;

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated() {
        mPresenter.attachView(this)
    }

    @Inject
    lateinit var mFragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return mFragmentDispatchingAndroidInjector
    }
}

