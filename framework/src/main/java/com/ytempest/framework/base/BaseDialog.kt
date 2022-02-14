package com.ytempest.framework.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.ytempest.framework.R
import com.ytempest.framework.binding.inflateViewBindingGeneric
import com.ytempest.tool.util.LogUtils

/**
 * @author heqidu
 * @since 21-2-8
 */
open class BaseDialog<VB : ViewBinding>(
        context: Context,
        themeResId: Int
) : Dialog(context, themeResId) {

    private val TAG = BaseDialog::class.java.simpleName
    lateinit var binding: VB

    constructor(context: Context) : this(context, R.style.common_dialog)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateViewBindingGeneric(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dismiss()
    }

    protected fun setBlockBackKey(block: Boolean) {
        setOnKeyListener { dialog, keyCode, event ->
            block
        }
    }

    fun setAutoDismiss(lifecycle: Lifecycle) {
        lifecycle.addObserver(GenericLifecycleObserver { source, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                dismiss()
            }
        })
    }

    override fun show() {
        try {
            if (!isShowing) {
                super.show()
            }
        } catch (e: Throwable) {
            if (LogUtils.isLoggable()) LogUtils.d(TAG, "show: e=$e")
        }
    }

    override fun dismiss() {
        try {
            if (isShowing) {
                super.dismiss()
            }
        } catch (e: Throwable) {
            if (LogUtils.isLoggable()) LogUtils.d(TAG, "dismiss: e=$e")
        }
    }
}