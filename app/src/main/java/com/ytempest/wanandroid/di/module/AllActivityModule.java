package com.ytempest.wanandroid.di.module;

import com.ytempest.wanandroid.activity.article.ArticleDetailActivity;
import com.ytempest.wanandroid.activity.login.LoginActivity;
import com.ytempest.wanandroid.activity.main.MainActivity;
import com.ytempest.wanandroid.activity.architecture.ArchitectureActivity;
import com.ytempest.wanandroid.activity.register.RegisterActivity;
import com.ytempest.wanandroid.base.activity.MvpActivity;
import com.ytempest.wanandroid.di.component.BaseActivityComponent;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author heqidu
 * @since 2020/6/28
 * <p>
 * {@link AllActivityModule}所属的Component为{@link BaseActivityComponent}的父Component
 * Module只是提供实例的类似一个映射关系的存在，它是Component的一部分。
 * 这里的Module是为了映射子Component{@link BaseActivityComponent}中定义的{@link MvpActivity}子类，
 * 它的作用就是为了让父Component【{@link AllActivityModule}所属的Component】能找到需要注入的子类
 * Activity，继而找到该子Activity要注入的实例的映射关系表，这个关系表一般用{@link ContributesAndroidInjector}声明
 */
@Module(subcomponents = {BaseActivityComponent.class})
public abstract class AllActivityModule {

    /**
     * 提供需要在{@link MvpActivity}的子类{@link MainActivity}中需要注入的实例的Module，
     * 即要注入到{@link MainActivity}的实例的映射关系表{@link ActivityModules.MainModule}
     */
    @ContributesAndroidInjector(modules = ActivityModules.MainModule.class)
    abstract MainActivity contributesMainActivityInjector();

    @ContributesAndroidInjector(modules = ActivityModules.EmptyModule.class)
    abstract LoginActivity contributesLoginActivityInjector();

    @ContributesAndroidInjector(modules = ActivityModules.EmptyModule.class)
    abstract RegisterActivity contributesRegisterActivityInjector();

    @ContributesAndroidInjector(modules = ActivityModules.EmptyModule.class)
    abstract ArticleDetailActivity contributesWebActivityInjector();

    @ContributesAndroidInjector(modules = ActivityModules.EmptyModule.class)
    abstract ArchitectureActivity contributesArchitectureActivityInjector();
}
