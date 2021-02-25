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
class BaseInteractor @Inject constructor(
        private val mHttpHelper: HttpHelper,
        private val mDbHelper: DbHelper,
        private val mConfigs: Configs,
) : MvpInteractor {

    override fun getHttpHelper(): HttpHelper {
        return mHttpHelper
    }

    override fun getDbHelper(): DbHelper {
        return mDbHelper
    }

    override fun getConfigs(): Configs {
        return mConfigs
    }
}