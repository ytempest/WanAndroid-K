package com.ytempest.wanandroid.di.module.http;

import com.ytempest.wanandroid.http.HttpApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author heqidu
 * @since 2020/7/4
 */
@Module
public class HttpModule {

    @Provides
    OkHttpClient provideOkHttpClient() {
        return OkHttpManager.getInstance().getClient();
    }


    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttp) {
        return new Retrofit.Builder()
                .baseUrl(HttpApi.BASE_URL)
                .client(okHttp)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    HttpApi provideHttpApi(Retrofit retrofit) {
        return retrofit.create(HttpApi.class);
    }
}
