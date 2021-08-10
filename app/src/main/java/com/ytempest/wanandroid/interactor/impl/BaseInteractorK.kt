package com.ytempest.wanandroid.interactor.impl

import com.ytempest.wanandroid.interactor.Configs
import com.ytempest.wanandroid.interactor.DbHelper
import com.ytempest.wanandroid.interactor.HttpHelper
import com.ytempest.wanandroid.interactor.configs.ConfigsImpl

/**
 * @author ytempest
 * @since 2021/8/9
 */
object BaseInteractorK {
    val httpHelper: HttpHelper = HttpHelperImpl()
    val dbHelper: DbHelper = DbHelperImpl()
    val configs: Configs = ConfigsImpl()
}