package com.ytempest.wanandroid.http.observer

import androidx.annotation.IntDef
import com.ytempest.wanandroid.base.view.IView
import com.ytempest.wanandroid.http.ErrCode
import com.ytempest.wanandroid.R

/**
 * @author heqidu
 * @since 21-2-7
 */


abstract class HandlerObserver<T>(
        private val mView: IView?,
        private val mFlags: Int = 0,
) : BaseObserver<T>() {

    constructor(view: IView?) : this(view, 0)

    private fun enable(@Flags flags: Int): Boolean {
        return (mFlags and flags) != 0
    }

    override fun onStart() {
        super.onStart()
        if (enable(Flags.SHOW_LOADING)) {
            mView?.showLoading()
        }
    }

    override fun onComplete() {
        super.onComplete()
        if (enable(Flags.SHOW_LOADING)) {
            mView?.stopLoading()
        }
    }

    override fun onFail(code: Int, e: Throwable) {
        mView?.let {
            if (enable(Flags.SHOW_LOADING)) {
                mView.stopLoading()

            } else if (enable(Flags.SHOW_ERR_MSG)) {
                when (code) {
                    ErrCode.NET_ERR -> mView.showToast(R.string.net_err)
                    ErrCode.EMPTY_RESP -> mView.showToast(R.string.get_data_fail)
                    ErrCode.DATA_ERR -> mView.showToast(R.string.request_fail)
                    else -> mView.showToast(R.string.unknown_err)
                }
            }
        }
    }

    @IntDef(
            Flags.SHOW_ERR_MSG, Flags.SHOW_LOADING
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Flags {
        companion object {
            const val SHOW_ERR_MSG: Int = 0x000001
            const val SHOW_LOADING = 0x000010
        }
    }

}




