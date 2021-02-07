package com.ytempest.wanandroid.interactor.configs.base;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;
import java.util.Set;

/**
 * @author heqidu
 * @since 2020/8/15
 */
public class PreferencesExtender {

    private final SharedPreferences mPreferences;

    public PreferencesExtender(SharedPreferences preferences) {
        mPreferences = preferences;
    }

    protected String getKey(String key) {
        return key;
    }

    /*Delegate*/

    public Editor edit() {
        return mPreferences.edit();
    }

    public Map<String, ?> getAll() {
        return mPreferences.getAll();
    }

    public boolean contains(String key) {
        return mPreferences.contains(getKey(key));
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    /*Async*/

    public void commit(Editor editor) {
        editor.apply();
    }

    public PreferencesExtender putBoolean(String key, boolean val) {
        Editor editor = edit().putBoolean(getKey(key), val);
        commit(editor);
        return this;
    }

    public PreferencesExtender putInt(String key, int val) {
        Editor editor = edit().putInt(getKey(key), val);
        commit(editor);
        return this;
    }

    public PreferencesExtender putFloat(String key, float val) {
        Editor editor = edit().putFloat(getKey(key), val);
        commit(editor);
        return this;
    }

    public PreferencesExtender putLong(String key, long val) {
        Editor editor = edit().putLong(getKey(key), val);
        commit(editor);
        return this;
    }

    public PreferencesExtender putString(String key, String val) {
        Editor editor = edit().putString(getKey(key), val);
        commit(editor);
        return this;
    }

    public PreferencesExtender putStringSet(String key, Set<String> val) {
        Editor editor = edit().putStringSet(getKey(key), val);
        commit(editor);
        return this;
    }

    public void clear() {
        Editor clear = edit().clear();
        commit(clear);
    }

    public void remove(String key) {
        Editor remove = edit().remove(getKey(key));
        commit(remove);
    }

    /*Sync*/

    public boolean commitSync(Editor editor) {
        return editor.commit();
    }

    public boolean putBooleanSync(String key, boolean val) {
        Editor editor = edit().putBoolean(getKey(key), val);
        return commitSync(editor);
    }

    public boolean putIntSync(String key, int val) {
        Editor editor = edit().putInt(getKey(key), val);
        return commitSync(editor);
    }

    public boolean putFloatSync(String key, float val) {
        Editor editor = edit().putFloat(getKey(key), val);
        return commitSync(editor);
    }

    public boolean putLongSync(String key, long val) {
        Editor editor = edit().putLong(getKey(key), val);
        return commitSync(editor);
    }

    public boolean putStringSync(String key, String val) {
        Editor editor = edit().putString(getKey(key), val);
        return commitSync(editor);
    }

    public boolean putStringSetSync(String key, Set<String> val) {
        Editor editor = edit().putStringSet(getKey(key), val);
        return commitSync(editor);
    }

    public boolean clearSync() {
        Editor clear = edit().clear();
        return commitSync(clear);
    }

    public boolean removeSync(String key) {
        Editor remove = edit().remove(getKey(key));
        return commitSync(remove);
    }

    /*Get*/

    public boolean getBoolean(String key, boolean defVal) {
        return mPreferences.getBoolean(getKey(key), defVal);
    }

    public int getInt(String key, int defVal) {
        return mPreferences.getInt(getKey(key), defVal);
    }

    public float getFloat(String key, float defVal) {
        return mPreferences.getFloat(getKey(key), defVal);
    }

    public long getLong(String key, long defVal) {
        return mPreferences.getLong(getKey(key), defVal);
    }

    public String getString(String key, String defVal) {
        return mPreferences.getString(getKey(key), defVal);
    }

    public Set<String> getStringSet(String key, Set<String> defVal) {
        return mPreferences.getStringSet(getKey(key), defVal);
    }
}
