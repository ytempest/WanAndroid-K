package com.ytempest.wanandroid.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.ytempest.wanandroid.binding.inflateViewBindingGeneric

/**
 * @author heqidu
 * @since 21-2-7
 */
abstract class AbstractActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateViewBindingGeneric(layoutInflater)
        setContentView(binding.root)
        onViewCreated()
    }


    abstract fun onViewCreated()
}