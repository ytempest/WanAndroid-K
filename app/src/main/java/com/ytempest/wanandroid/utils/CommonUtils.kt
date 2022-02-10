package com.ytempest.wanandroid.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @author qiduhe
 * @since 2021/8/10
 */
object CommonUtils {
    /**
     * 自动对焦到输入框，并打开软键盘
     */
    fun showSoftKeyBoard(editText: EditText, delay: Long = 200) {
        // 延迟200毫秒弹出，以防止自动弹出失效
        editText.postDelayed({
            val inputMethodManager = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.let {
                editText.requestFocus()
                inputMethodManager.showSoftInput(editText, 0)
            }
        }, delay)
    }

}