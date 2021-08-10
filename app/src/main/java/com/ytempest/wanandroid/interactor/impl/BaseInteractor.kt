package com.ytempest.wanandroid.interactor.impl

import com.ytempest.wanandroid.interactor.Configs
import com.ytempest.wanandroid.interactor.DbHelper
import com.ytempest.wanandroid.interactor.HttpHelper
import com.ytempest.wanandroid.interactor.MvpInteractor

import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-8
 */
class BaseInteractor @Inject constructor() : MvpInteractor {

    override fun getHttpHelper(): HttpHelper {
        return BaseInteractorK.httpHelper
    }

    override fun getDbHelper(): DbHelper {
        return BaseInteractorK.dbHelper
    }

    override fun getConfigs(): Configs {
        return BaseInteractorK.configs
    }
}