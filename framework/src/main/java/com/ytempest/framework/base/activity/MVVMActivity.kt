package com.ytempest.framework.base.activity

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

/**
 * @author qiduhe
 * @since 2021/8/10
 */
abstract class MVVMActivity<VB : ViewBinding> : AbstractActivity<VB>() {

    abstract val viewModel: ViewModel
}