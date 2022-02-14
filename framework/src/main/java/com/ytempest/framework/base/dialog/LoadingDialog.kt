package com.ytempest.framework.base.dialog

import android.content.Context
import com.ytempest.framework.R
import com.ytempest.framework.base.BaseDialog
import com.ytempest.framework.databinding.DialogLoadingBinding


/**
 * @author heqidu
 * @since 21-2-22
 */
class LoadingDialog(context: Context) :
    BaseDialog<DialogLoadingBinding>(context, R.style.common_dialog_transparent) {

    init {
        setCanceledOnTouchOutside(false)
    }
}