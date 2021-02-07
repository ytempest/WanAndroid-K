package com.ytempest.wanandroid.di.module.http;

import android.text.TextUtils;

import com.ytempest.tool.util.NetUtils;
import com.ytempest.wanandroid.base.WanApp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author heqidu
 * @since 2020/7/6
 */
public class CacheInterceptor implements Interceptor {

    private static final String CACHE_CONTROL = "Cache-Control";
    private static final long MAX_AGE_TIME = TimeUnit.MINUTES.toSeconds(6);

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 判断请求是否需要使用缓存
        Request request = chain.request();
        CacheControl reqCacheControl = request.cacheControl();
        boolean noCache = reqCacheControl.noCache() || reqCacheControl.noStore() || reqCacheControl.maxAgeSeconds() == 0;
        if (NetUtils.isNetAvailable(WanApp.getApp()) && !noCache) {
            request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        // 对响应添加缓存过期时间控制
        Response response = chain.proceed(request);
        // 服务器没有缓存控制，则客户端自己添加控制
        if (TextUtils.isEmpty(response.header(CACHE_CONTROL))) {
            // 优先使用请求头中的缓存控制
            String cacheControl = request.header(CACHE_CONTROL);
            if (TextUtils.isEmpty(cacheControl)) {
                cacheControl = "public, max-age=" + MAX_AGE_TIME;
            }
            response = response.newBuilder()
                    .addHeader(CACHE_CONTROL, cacheControl)
                    .build();
        }

        return response;
    }
}
