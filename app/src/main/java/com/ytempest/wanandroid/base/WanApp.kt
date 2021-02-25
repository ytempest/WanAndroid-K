package com.ytempest.wanandroid.base

import android.app.Activity
import android.app.Application
import com.ytempest.tool.ToolModule
import com.ytempest.tool.util.LogUtils
import com.ytempest.wanandroid.di.component.AppComponent
import com.ytempest.wanandroid.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-7
 */
class WanApp : Application(), HasActivityInjector {

    companion object {
        lateinit var instance: WanApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        ToolModule.init(this)
        LogUtils.setLogPrefix("[WanAndroid]: ")
        LogUtils.setLoggable(true)

        val appComponent: AppComponent = DaggerAppComponent.builder().build()
        appComponent.inject(this)
    }

    @Inject
    lateinit var mAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return mAndroidInjector
    }
}