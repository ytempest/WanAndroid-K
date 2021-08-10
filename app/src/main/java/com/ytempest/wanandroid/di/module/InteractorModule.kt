package com.ytempest.wanandroid.di.module

import com.ytempest.wanandroid.interactor.impl.BaseInteractor
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
    fun provideBaseInteractor(): BaseInteractor {
        return BaseInteractor()
    }

}