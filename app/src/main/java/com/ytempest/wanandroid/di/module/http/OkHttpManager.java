package com.ytempest.wanandroid.di.module.http;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.ytempest.wanandroid.BuildConfig;
import com.ytempest.wanandroid.base.WanApp;
import com.ytempest.wanandroid.di.module.http.event.NetEventListener;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author heqidu
 * @since 2020/10/9
 */
public class OkHttpManager {
    private volatile static OkHttpManager sInstance = null;

    public static OkHttpManager getInstance() {
        if (sInstance == null) {
            synchronized (OkHttpManager.class) {
                if (sInstance == null) {
                    sInstance = new OkHttpManager();
                }
            }
        }
        return sInstance;
    }

    private static final long CACHE_SIZE = 50 * 1024 * 1024;
    private final OkHttpClient mClient;

    private OkHttpManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // 添加网络请求监控
            builder.eventListenerFactory(NetEventListener.FACTORY);
            // 添加http日志
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }

        // 添加缓存控制
        File cacheDir = WanApp.getApp().getCacheDir();
        Cache cache = new Cache(cacheDir, CACHE_SIZE);
        builder.cache(cache);
        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        builder.addInterceptor(cacheInterceptor);
        builder.addNetworkInterceptor(cacheInterceptor);

        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);

        //错误重连
        builder.retryOnConnectionFailure(true);
        //cookie认证
        builder.cookieJar(new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(WanApp.getApp())));
        mClient = builder.build();
    }

    public OkHttpClient getClient() {
        return mClient;
    }
}
