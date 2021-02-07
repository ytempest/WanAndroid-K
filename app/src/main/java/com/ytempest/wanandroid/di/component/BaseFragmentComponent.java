package com.ytempest.wanandroid.di.component;

import com.ytempest.wanandroid.base.fragment.MvpFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * @author heqidu
 * @since 2020/6/30
 */
@Subcomponent(modules = AndroidInjectionModule.class)
public interface BaseFragmentComponent extends AndroidInjector<MvpFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MvpFragment> {
    }
}
