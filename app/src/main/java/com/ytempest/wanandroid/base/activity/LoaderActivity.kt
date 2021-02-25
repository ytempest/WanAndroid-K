package com.ytempest.wanandroid.base.activity

import android.view.ViewGroup
import com.ytempest.wanandroid.base.presenter.IPresenter
import com.ytempest.wanandroid.base.load.Loader

/**
 * @author heqidu
 * @since 21-2-8
 */
abstract class LoaderActivity<Presenter : IPresenter> : MvpActivity<Presenter>() {
    private lateinit var mLoader: Loader

    override fun onViewCreated() {
        super.onViewCreated()
        val root = findViewById<ViewGroup>(android.R.id.content)
        mLoader = Loader(root)
        mLoader.setReloadCall { onReloadClick() }
    }

    protected open fun onReloadClick() {

    }

    fun getLoader(): Loader {
        return mLoader
    }
}