package com.ytempest.wanandroid.interactor.configs

import com.ytempest.wanandroid.interactor.Configs
import javax.inject.Inject

/**
 * @author heqidu
 * @since 21-2-8
 */
class ConfigsImpl @Inject constructor() : Configs {

    override fun getUser(): UserConfig {
        return UserConfig()
    }
}