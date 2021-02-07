package com.ytempest.wanandroid.interactor.configs.base;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author heqidu
 * @since 2020/8/15
 */
@StringDef({
        PreferencesName.USER,
})
@Retention(RetentionPolicy.SOURCE)
public @interface PreferencesName {
    String USER = "config_user";
}
