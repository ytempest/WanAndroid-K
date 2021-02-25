package com.ytempest.wanandroid.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ytempest.layoutinjector.LayoutInjector

/**
 * @author heqidu
 * @since 21-2-7
 */
abstract class AbstractFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInjector.inject(this, inflater, container)
    }

}