package com.ytempest.wanandroid.interactor.configs.base

import android.content.Context
import android.content.SharedPreferences

/**
 * @author heqidu
 * @since 21-2-8
 */
abstract class AbsConfig(
        context: Context,
        @PreferencesName name: String, ) {

    protected val mPref: PreferencesExtender;

    init {
        val preferences = context.applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE)
        mPref = createPreferencesExtender(preferences)
    }

    protected open fun createPreferencesExtender(preferences: SharedPreferences): PreferencesExtender {
        return PreferencesExtender(preferences)
    }

}