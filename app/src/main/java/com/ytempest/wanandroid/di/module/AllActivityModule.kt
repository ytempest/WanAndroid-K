package com.ytempest.wanandroid.di.module

import com.ytempest.wanandroid.activity.article.ArticleDetailActivity
import com.ytempest.wanandroid.di.component.BaseActivityComponent
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author heqidu
 * @since 21-2-7
 */
@Module(subcomponents = [BaseActivityComponent::class])
abstract class AllActivityModule {

    @ContributesAndroidInjector(modules = [FragEmptyModule::class])
    abstract fun contributesWebActivityInjector(): ArticleDetailActivity
}