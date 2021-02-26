package com.ytempest.wanandroid.dialog

import android.content.Context
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.base.BaseDialog
import com.ytempest.wanandroid.databinding.DialogLoadingBinding

/**
 * @author heqidu
 * @since 21-2-22
 */
class LoadingDialog(context: Context) : BaseDialog<DialogLoadingBinding>(context, R.style.common_dialog_transparent) {

    init {
        setCanceledOnTouchOutside(false)
    }
}