package com.ytempest.wanandroid.di.module

import com.ytempest.wanandroid.interactor.Configs
import com.ytempest.wanandroid.interactor.DbHelper
import com.ytempest.wanandroid.interactor.HttpHelper
import com.ytempest.wanandroid.interactor.configs.ConfigsImpl
import com.ytempest.wanandroid.interactor.impl.BaseInteractor
import com.ytempest.wanandroid.interactor.impl.DbHelperImpl
import com.ytempest.wanandroid.interactor.impl.HttpHelperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author heqidu
 * @since 21-2-7
 */
@Module
class InteractorModule {
    @Provides
    @Singleton
    fun provideHttpHelper(httpHelperImpl: HttpHelperImpl): HttpHelper {
        return httpHelperImpl
    }

    @Provides
    @Singleton
    fun provideDBHelper(dbHelperImpl: DbHelperImpl): DbHelper {
        return dbHelperImpl
    }

    @Provides
    @Singleton
    fun provideConfigs(configsImpl: ConfigsImpl): Configs {
        return configsImpl
    }

    @Provides
    @Singleton
    fun provideBaseInteractor(httpHelper: HttpHelper, dbHelper: DbHelper, configs: Configs): BaseInteractor {
        return BaseInteractor(httpHelper, dbHelper, configs)

    }

}