package com.ytempest.wanandroid.dialog

import android.content.Context
import com.ytempest.layoutinjector.annotation.InjectLayout
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.base.BaseDialog

/**
 * @author heqidu
 * @since 21-2-22
 */
@InjectLayout(R.layout.dialog_loading)
class LoadingDialog(context: Context) : BaseDialog(context, R.style.common_dialog_transparent) {

    init {
        setCanceledOnTouchOutside(false)
    }
}