package com.ytempest.wanandroid.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.ytempest.wanandroid.base.activity.MVVMActivity
import com.ytempest.wanandroid.base.fragment.MVVMFragment

/**
 * @author qiduhe
 * @since 2021/8/10
 */

inline fun <reified VM : ViewModel> MVVMActivity<*>.createViewModel(): VM {
    return ViewModelProviders.of(this).get(VM::class.java)
}

inline fun <reified VM : ViewModel> MVVMFragment<*>.createViewModel(): VM {
    return ViewModelProviders.of(this).get(VM::class.java)
}
