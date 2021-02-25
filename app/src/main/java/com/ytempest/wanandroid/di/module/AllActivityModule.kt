package com.ytempest.wanandroid.di.module

import com.ytempest.wanandroid.activity.architecture.ArchitectureActivity
import com.ytempest.wanandroid.activity.article.ArticleDetailActivity
import com.ytempest.wanandroid.activity.login.LoginActivity
import com.ytempest.wanandroid.activity.main.MainActivity
import com.ytempest.wanandroid.activity.register.RegisterActivity
import com.ytempest.wanandroid.di.component.BaseActivityComponent
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author heqidu
 * @since 21-2-7
 */
@Module(subcomponents = [BaseActivityComponent::class])
abstract class AllActivityModule {


    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun contributesMainActivityInjector(): MainActivity

    @ContributesAndroidInjector(modules = [FragEmptyModule::class])
    abstract fun contributesLoginActivityInjector(): LoginActivity

    @ContributesAndroidInjector(modules = [FragEmptyModule::class])
    abstract fun contributesRegisterActivityInjector(): RegisterActivity

    @ContributesAndroidInjector(modules = [FragEmptyModule::class])
    abstract fun contributesWebActivityInjector(): ArticleDetailActivity

    @ContributesAndroidInjector(modules = [FragEmptyModule::class])
    abstract fun contributesArchitectureActivityInjector(): ArchitectureActivity
}