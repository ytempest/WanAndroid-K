package com.ytempest.wanandroid.di.component

import com.ytempest.wanandroid.base.activity.MvpActivity
import dagger.Subcomponent
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

/**
 * @author heqidu
 * @since 21-2-7
 */
@Subcomponent(modules = [AndroidInjectionModule::class])
interface BaseActivityComponent : AndroidInjector<MvpActivity<*>> {
    /**
     * 每一个继承于[MvpActivity]的Activity都会继承于这个一个子组件
     */
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MvpActivity<*>>() {

    }
}