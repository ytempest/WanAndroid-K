package com.ytempest.wanandroid.di.module.http

import com.ytempest.wanandroid.Constants
import com.ytempest.wanandroid.http.HttpApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author heqidu
 * @since 21-2-7
 */
class HttpModule {

    private object Holder {
        val instance = HttpModule()
    }

    companion object {
        val instance = Holder.instance
    }

    private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.Http.BASE_URL)
            .client(OkHttpManager.instance.getClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getHttpApi(): HttpApi {
        return retrofit.create(HttpApi::class.java)
    }
}