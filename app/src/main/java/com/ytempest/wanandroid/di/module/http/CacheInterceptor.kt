package com.ytempest.wanandroid.di.module.http

import com.ytempest.wanandroid.base.WanApp
import com.ytempest.tool.util.NetUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * @author heqidu
 * @since 21-2-7
 */
class CacheInterceptor : Interceptor {

    private val CACHE_CONTROL = "Cache-Control"
    private val MAX_AGE_TIME = TimeUnit.MINUTES.toSeconds(6)

    override fun intercept(chain: Interceptor.Chain): Response {
        // 判断请求是否需要使用缓存
        val request = chain.request()
        val reqCacheControl = request.cacheControl()
        val noCache = reqCacheControl.noCache() || reqCacheControl.noStore()
        if (NetUtils.isNetAvailable(WanApp.instance) && !noCache) {
            request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
        }
        // 对响应添加缓存过期时间控制
        var response = chain.proceed(request)
        // 服务器没有缓存控制，则客户端自己添加控制
        if (response.header(CACHE_CONTROL).isNullOrEmpty()) {
            // 优先使用请求头中的缓存控制
            var cacheControl = request.header(CACHE_CONTROL)
            if (cacheControl.isNullOrEmpty()) {
                cacheControl = "public, max-age=$MAX_AGE_TIME"
            }
            response = response.newBuilder()
                    .addHeader(CACHE_CONTROL, cacheControl)
                    .build()
        }
        return response
    }

}