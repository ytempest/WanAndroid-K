package com.ytempest.wanandroid.di.component

import com.ytempest.wanandroid.base.WanApp
import com.ytempest.wanandroid.di.module.AllActivityModule
import com.ytempest.wanandroid.di.module.AllFragmentModule
import com.ytempest.wanandroid.di.module.InteractorModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


/**
 * @author heqidu
 * @since 21-2-7
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    AllActivityModule::class,
    AllFragmentModule::class,
    InteractorModule::class
])
interface AppComponent {
    fun inject(app: WanApp)
}