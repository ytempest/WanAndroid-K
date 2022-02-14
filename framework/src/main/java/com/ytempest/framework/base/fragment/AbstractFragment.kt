package com.ytempest.framework.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ytempest.tool.util.ToastUtils
import com.ytempest.framework.base.view.IView
import com.ytempest.framework.binding.inflateViewBindingGeneric
import com.ytempest.framework.base.dialog.LoadingDialog

/**
 * @author heqidu
 * @since 21-2-7
 */
abstract class AbstractFragment<VB : ViewBinding> : Fragment(), IView {

    private var _binding: VB? = null
    val binding: VB by lazy { _binding!! }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflateViewBindingGeneric<VB>(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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