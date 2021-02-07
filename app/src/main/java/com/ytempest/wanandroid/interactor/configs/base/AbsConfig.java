package com.ytempest.wanandroid.interactor.configs.base;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author heqidu
 * @since 2020/8/15
 */
public abstract class AbsConfig {

    protected final PreferencesExtender mPref;

    public AbsConfig(Context context, @PreferencesName String name) {
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences(name, Context.MODE_PRIVATE);
        mPref = createPreferencesExtender(preferences);
    }

    protected PreferencesExtender createPreferencesExtender(SharedPreferences preferences) {
        return new PreferencesExtender(preferences);
    }

}
