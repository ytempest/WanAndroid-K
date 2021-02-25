package com.ytempest.wanandroid.di.module.http

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.ytempest.wanandroid.base.WanApp
import com.ytempest.wanandroid.di.module.http.event.NetEventListener
import com.ytempest.wanandroid.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


/**
 * @author heqidu
 * @since 21-2-7
 */
class OkHttpManager private constructor() {

    companion object {
        val instance: OkHttpManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            OkHttpManager()
        }
    }

    private val CACHE_SIZE = 50 * 1024 * 1024L
    private val mClient: OkHttpClient

    init {
        mClient = with(OkHttpClient.Builder()) {

            if (BuildConfig.DEBUG) {
                // 添加网络请求监控
                eventListenerFactory(NetEventListener.FACTORY)
                // 添加http日志
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
                addInterceptor(loggingInterceptor)
            }

            // 添加缓存控制
            val cacheDir = WanApp.instance.cacheDir
            val cache = Cache(cacheDir, CACHE_SIZE)
            cache(cache)

            val cacheInterceptor = CacheInterceptor()
            addInterceptor(cacheInterceptor)
            addNetworkInterceptor(cacheInterceptor)

            //设置超时
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(20, TimeUnit.SECONDS)

            //错误重连
            retryOnConnectionFailure(true)

            //cookie认证
            cookieJar(PersistentCookieJar(SetCookieCache(),
                    SharedPrefsCookiePersistor(WanApp.instance)))
        }.build()
    }

    fun getClient(): OkHttpClient {
        return mClient
    }

}