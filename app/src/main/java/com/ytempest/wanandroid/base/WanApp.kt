package com.ytempest.wanandroid.base

import android.app.Application
import com.ytempest.tool.ToolModule
import com.ytempest.tool.util.LogUtils

/**
 * @author heqidu
 * @since 21-2-7
 */
class WanApp : Application() {

    companion object {
        lateinit var instance: WanApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        ToolModule.init(this)
        LogUtils.setLogPrefix("[WanAndroid]: ")
        LogUtils.setLoggable(true)
    }
}