package com.ytempest.wanandroid.di.component

import com.ytempest.wanandroid.base.fragment.MvpFragment
import dagger.Subcomponent
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

/**
 * @author heqidu
 * @since 21-2-7
 */
@Subcomponent(modules = [AndroidInjectionModule::class])
interface BaseFragmentComponent : AndroidInjector<MvpFragment<*, *>> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MvpFragment<*, *>>() {
    }
}