package com.ytempest.wanandroid.base.load

import androidx.annotation.IntDef

/**
 * @author heqidu
 * @since 21-2-8
 */
@IntDef(ViewType.ERR, ViewType.LOAD)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class ViewType {
    companion object {
        const val ERR = 1
        const val LOAD = 2
    }
}
