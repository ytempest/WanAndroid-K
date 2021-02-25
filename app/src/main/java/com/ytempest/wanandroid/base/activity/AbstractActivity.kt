package com.ytempest.wanandroid.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ytempest.layoutinjector.LayoutInjector

/**
 * @author heqidu
 * @since 21-2-7
 */
abstract class AbstractActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LayoutInjector.inject(this)
        onViewCreated()
    }


    abstract fun onViewCreated()
}