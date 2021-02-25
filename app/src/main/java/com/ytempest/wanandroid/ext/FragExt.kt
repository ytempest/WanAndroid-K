package com.ytempest.wanandroid.ext

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * @author heqidu
 * @since 21-2-7
 */
fun Fragment.getBundle(): Bundle {
    var bundle = arguments
    if (bundle == null) {
        bundle = Bundle()
        arguments = bundle
    }
    return bundle
}
