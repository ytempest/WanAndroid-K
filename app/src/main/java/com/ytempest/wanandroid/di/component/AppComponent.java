package com.ytempest.wanandroid.di.component;

import com.ytempest.wanandroid.base.WanApp;
import com.ytempest.wanandroid.di.module.AllActivityModule;
import com.ytempest.wanandroid.di.module.AllFragmentModule;
import com.ytempest.wanandroid.di.module.InteractorModule;
import com.ytempest.wanandroid.di.module.http.HttpModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author heqidu
 * @since 2020/6/28
 */
@Singleton
@Component(modules = {AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        AllActivityModule.class,
        AllFragmentModule.class,
        InteractorModule.class,
        HttpModule.class,
})
public interface AppComponent {
    void inject(WanApp app);
}