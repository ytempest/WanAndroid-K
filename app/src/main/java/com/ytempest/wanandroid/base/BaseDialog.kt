package com.ytempest.wanandroid.base

import android.app.Dialog
import android.content.Context
import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import com.ytempest.layoutinjector.LayoutInjector
import com.ytempest.tool.util.LogUtils
import com.ytempest.wanandroid.R

/**
 * @author heqidu
 * @since 21-2-8
 */
open class BaseDialog(
        context: Context,
        themeResId: Int
) : Dialog(context, themeResId) {

    private val TAG = BaseDialog::class.java.simpleName

    constructor(context: Context) : this(context, R.style.common_dialog)

    init {
        LayoutInjector.inject(this)
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
            LogUtils.d(TAG, "show: e=$e")
        }
    }

    override fun dismiss() {
        try {
            if (isShowing) {
                super.dismiss()
            }
        } catch (e: Throwable) {
            LogUtils.d(TAG, "dismiss: e=$e")
        }
    }
}