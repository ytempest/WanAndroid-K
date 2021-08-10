package com.ytempest.wanandroid.interactor.configs

import com.ytempest.wanandroid.interactor.Configs

/**
 * @author heqidu
 * @since 21-2-8
 */
class ConfigsImpl : Configs {

    override fun getUser(): UserConfig {
        return UserConfig()
    }
}