package com.ytempest.wanandroid.di.module;

import com.ytempest.wanandroid.activity.architecture.content.ArchArticleFrag;
import com.ytempest.wanandroid.activity.main.home.HomeFrag;
import com.ytempest.wanandroid.activity.main.knowledge.KnowledgeFrag;
import com.ytempest.wanandroid.activity.main.navigation.NavigationFrag;
import com.ytempest.wanandroid.activity.main.project.ProjectFrag;
import com.ytempest.wanandroid.activity.main.project.content.ProjectContentFrag;
import com.ytempest.wanandroid.di.component.BaseFragmentComponent;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author heqidu
 * @since 2020/6/30
 */
@Module(subcomponents = BaseFragmentComponent.class)
public abstract class AllFragmentModule {

    @ContributesAndroidInjector(modules = FragmentModules.EmptyModule.class)
    abstract HomeFrag contributeHomeFragInjector();

    @ContributesAndroidInjector(modules = FragmentModules.EmptyModule.class)
    abstract KnowledgeFrag contributeKnowledgeFragInjector();

    @ContributesAndroidInjector(modules = FragmentModules.EmptyModule.class)
    abstract NavigationFrag contributeNavigationFragInjector();

    @ContributesAndroidInjector(modules = FragmentModules.EmptyModule.class)
    abstract ProjectFrag contributeProjectFragInjector();

    @ContributesAndroidInjector(modules = FragmentModules.EmptyModule.class)
    abstract ProjectContentFrag contributeClassifyContentFragInjector();

    @ContributesAndroidInjector(modules = FragmentModules.EmptyModule.class)
    abstract ArchArticleFrag contributeArchArticleFragInjector();
}
