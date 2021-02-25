package com.ytempest.wanandroid.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ytempest.wanandroid.binding.inflateViewBindingGeneric

/**
 * @author heqidu
 * @since 21-2-7
 */
abstract class AbstractFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding: VB by lazy { _binding!! }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflateViewBindingGeneric<VB>(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}