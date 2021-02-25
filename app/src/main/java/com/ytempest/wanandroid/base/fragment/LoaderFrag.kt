package com.ytempest.wanandroid.base.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.ytempest.wanandroid.base.load.Loader
import com.ytempest.wanandroid.base.load.ViewType
import com.ytempest.wanandroid.base.presenter.IPresenter

/**
 * @author heqidu
 * @since 21-2-8
 */
abstract class LoaderFrag<Presenter : IPresenter, VB : ViewBinding> : MvpFragment<Presenter, VB>() {
    private lateinit var mLoader: Loader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoader = Loader(view as ViewGroup)
        mLoader.setReloadCall {
            mLoader.showView(ViewType.LOAD)
            onReloadClick()
        }
    }

    protected open fun onReloadClick() {
    }

    fun getLoader(): Loader {
        return mLoader
    }
}