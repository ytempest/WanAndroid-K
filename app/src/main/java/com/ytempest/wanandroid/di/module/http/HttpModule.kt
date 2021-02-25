package com.ytempest.wanandroid.di.module.http

import com.ytempest.wanandroid.Constants
import com.ytempest.wanandroid.http.HttpApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @author heqidu
 * @since 21-2-7
 */
@Module
class HttpModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpManager.instance.getClient()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.Http.BASE_URL)
                .client(okHttp)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Singleton
    @Provides
    fun provideHttpApi(retrofit: Retrofit): HttpApi {
        return retrofit.create(HttpApi::class.java)
    }
}