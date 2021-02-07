package com.ytempest.wanandroid.di.component;

import com.ytempest.wanandroid.base.activity.MvpActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * {@link MvpActivity}子类Activity使用Dagger注入时的模板Activity，用于统一生成子类的Subcomponent，
 * 如MainActivity则如下：
 * // @Subcomponent(modules = Modules.MainModule.class)
 * // public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {
 * //     @Subcomponent.Builder
 * //     abstract class Builder extends AndroidInjector.Builder<MainActivity> {
 * //     }
 * // }
 */
@Subcomponent(modules = {AndroidInjectionModule.class})
public interface BaseActivityComponent extends AndroidInjector<MvpActivity> {

    /**
     * 每一个继承于{@link MvpActivity}的Activity都会继承于这个一个子组件
     */
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MvpActivity> {
    }
}