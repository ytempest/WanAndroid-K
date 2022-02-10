package com.ytempest.wanandroid.di.module

import com.ytempest.wanandroid.activity.main.home.HomeFrag
import com.ytempest.wanandroid.activity.main.knowledge.KnowledgeFrag
import com.ytempest.wanandroid.activity.main.navigation.NavigationFrag
import com.ytempest.wanandroid.activity.main.project.ProjectFrag
import com.ytempest.wanandroid.activity.main.project.content.ProjectContentFrag
import com.ytempest.wanandroid.di.component.BaseFragmentComponent
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author heqidu
 * @since 21-2-7
 */
@Module(subcomponents = [BaseFragmentComponent::class])
abstract class AllFragmentModule {
    @ContributesAndroidInjector(modules = [FragEmptyModule::class])
    abstract fun contributeHomeFragInjector(): HomeFrag

    @ContributesAndroidInjector(modules = [FragEmptyModule::class])
    abstract fun contributeKnowledgeFragInjector(): KnowledgeFrag

    @ContributesAndroidInjector(modules = [FragEmptyModule::class])
    abstract fun contributeNavigationFragInjector(): NavigationFrag

    @ContributesAndroidInjector(modules = [FragEmptyModule::class])
    abstract fun contributeProjectFragInjector(): ProjectFrag

    @ContributesAndroidInjector(modules = [FragEmptyModule::class])
    abstract fun contributeClassifyContentFragInjector(): ProjectContentFrag
}