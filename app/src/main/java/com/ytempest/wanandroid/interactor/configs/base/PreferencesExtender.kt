package com.ytempest.wanandroid.interactor.configs.base

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener

/**
 * @author heqidu
 * @since 21-2-8
 */
open class PreferencesExtender(private val mPreferences: SharedPreferences) {

    protected open fun getKey(key: String): String = key

    /*Delegate*/

    fun edit(): SharedPreferences.Editor = mPreferences.edit()

    fun getAll(): Map<String, *> = mPreferences.all

    fun contains(key: String): Boolean = mPreferences.contains(getKey(key))

    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        mPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener?) {
        mPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun commit(editor: SharedPreferences.Editor) {
        editor.apply()
    }

    fun clear() {
        edit().clear().apply()
    }

    fun clearSync() = edit().clear().commit()

    fun putVal(key: String, value: Any) {
        putEdit(key, value).apply()
    }

    fun putValSync(key: String, value: Any) {
        putEdit(key, value).commit()
    }

    private fun putEdit(key: String, value: Any): SharedPreferences.Editor = with(edit()) {
        when (value) {
            is Boolean -> putBoolean(key, value)
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Set<*> -> putStringSet(key, value as Set<String>)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }
    }

    /*Get*/

    fun <T> getVal(key: String, defVal: T): T = with(mPreferences) {
        val res: Any = when (defVal) {
            is Boolean -> getBoolean(key, defVal)
            is Int -> getInt(key, defVal)
            is Float -> getFloat(key, defVal)
            is Long -> getLong(key, defVal)
            is String -> getString(key, defVal)!!
            is Set<*> -> getStringSet(key, defVal as Set<String>)!!
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }
        res as T
    }
}