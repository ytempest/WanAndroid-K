package com.ytempest.wanandroid.interactor.configs.base

import androidx.annotation.StringDef

/**
 * @author heqidu
 * @since 21-2-8
 */
@StringDef(
        PreferencesName.USER,
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class PreferencesName {
    companion object {
        const val USER = "config_user"
    }
}