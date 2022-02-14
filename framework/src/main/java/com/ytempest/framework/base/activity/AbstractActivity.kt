package com.ytempest.framework.base.activity

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.ytempest.framework.base.dialog.LoadingDialog
import com.ytempest.framework.base.view.IView
import com.ytempest.framework.binding.inflateViewBindingGeneric
import com.ytempest.tool.util.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author heqidu
 * @since 21-2-7
 */
abstract class AbstractActivity<VB : ViewBinding> : AppCompatActivity(), IView,
    CoroutineScope by MainScope() {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateViewBindingGeneric(layoutInflater)
        setContentView(binding.root)
        onViewCreated()
    }


    abstract fun onViewCreated()

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    /*View*/

    // TODO: 确认懒加载出来的是不是同一个实例
    private val mLoadingDialog: LoadingDialog by lazy {
        LoadingDialog(this).apply { setAutoDismiss(lifecycle) }
    }

    override fun showLoading() {
        mLoadingDialog.show()
    }

    override fun stopLoading() {
        mLoadingDialog.dismiss()
    }


    override fun showToast(msg: String?) {
        ToastUtils.show(this, msg)
    }

    override fun showToast(@StringRes textId: Int) {
        ToastUtils.show(this, textId)
    }

}