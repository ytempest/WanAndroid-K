package com.ytempest.wanandroid.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.StringRes

/**
 * @author heqidu
 * @since 21-2-7
 */
val View.ctx: Context
    get() = context

fun View.getString(@StringRes id: Int): String = this.resources.getString(id)


fun Activity.getActivity(): Activity {
    return this;
}

fun Intent?.getIntSafe(key: String, defVal: Int): Int {
    return this?.getIntExtra(key, defVal) ?: defVal
}

fun Intent?.getFloatSafe(key: String, defVal: Float): Float {
    return this?.getFloatExtra(key, defVal) ?: defVal
}

fun Intent?.getLongSafe(key: String, defVal: Long): Long {
    return this?.getLongExtra(key, defVal) ?: defVal
}

fun Intent?.getBooleanSafe(key: String, defVal: Boolean): Boolean {
    return this?.getBooleanExtra(key, defVal) ?: defVal
}

fun Intent?.getStringSafe(key: String, defVal: String?): String? {
    return this?.getStringExtra(key) ?: defVal
}

