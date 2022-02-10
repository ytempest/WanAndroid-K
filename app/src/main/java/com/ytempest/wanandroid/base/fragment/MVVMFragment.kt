package com.ytempest.wanandroid.base.fragment

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

/**
 * @author qiduhe
 * @since 2021/8/10
 */
abstract class MVVMFragment<VB : ViewBinding> : AbstractFragment<VB>() {

    abstract val viewModel: ViewModel
}