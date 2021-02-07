package com.ytempest.wanandroid.base;

import android.app.Activity;
import android.app.Application;

import com.ytempest.tool.ToolModule;
import com.ytempest.tool.util.LogUtils;
import com.ytempest.wanandroid.di.component.AppComponent;
import com.ytempest.wanandroid.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * @author heqidu
 * @since 2020/6/22
 */
public class WanApp extends Application implements HasActivityInjector {

    private static WanApp sInstance;

    public static WanApp getApp() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ToolModule.init(this);
        LogUtils.setLogPrefix("[WanAndroid]: ");
        LogUtils.setLoggable(true);

        AppComponent appComponent = DaggerAppComponent.builder().build();
        appComponent.inject(this);
    }

    @Inject
    DispatchingAndroidInjector<Activity> mAndroidInjector;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mAndroidInjector;
    }
}
