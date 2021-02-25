package com.ytempest.wanandroid.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.viewbinding.ViewBinding
import com.ytempest.tool.util.ToastUtils
import com.ytempest.wanandroid.base.presenter.IPresenter
import com.ytempest.wanandroid.base.view.IView
import com.ytempest.wanandroid.dialog.LoadingDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-7
 */
abstract class MvpFragment<Presenter : IPresenter, VB : ViewBinding> : AbstractFragment<VB>(), IView {

    @Inject
    protected lateinit var mPresenter: Presenter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)
    }

    override fun onDestroyView() {
        mPresenter.detach()
        super.onDestroyView()
    }

    override fun showToast(msg: String?) {
        ToastUtils.show(context, msg)
    }

    override fun showToast(@StringRes textId: Int) {
        ToastUtils.show(context, textId)
    }

    private val mLoadingDialog: LoadingDialog by lazy {
        val dialog = LoadingDialog(requireActivity())
        dialog.setAutoDismiss(lifecycle)
        dialog
    }

    override fun showLoading() {
        mLoadingDialog.show()
    }

    override fun stopLoading() {
        mLoadingDialog.dismiss()
    }
}