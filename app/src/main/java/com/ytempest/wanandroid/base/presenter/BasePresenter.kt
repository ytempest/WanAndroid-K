package com.ytempest.wanandroid.base.presenter

import com.ytempest.wanandroid.base.view.IView
import com.ytempest.wanandroid.interactor.impl.BaseInteractor
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy


/**
 * @author heqidu
 * @since 21-2-7
 */
abstract class BasePresenter<View : IView> : IPresenter {


    protected val mInteractor: BaseInteractor
    private var mSrcView: View? = null
    protected lateinit var mView: View

    constructor(interactor: BaseInteractor) {
        mInteractor = interactor
    }

    override fun <V : IView> attachView(view: V) {
        mSrcView = view as View
        mView = mSrcView as View
        // FIXME
        if (true) {
            return
        }
        Proxy.newProxyInstance(view.javaClass.classLoader,
                view.javaClass.interfaces,
                object : InvocationHandler {
                    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
                        if (mSrcView != null) {
                            return method!!.invoke(proxy, *(args ?: emptyArray()))
                        }
                        return Any()
                    }
                }) as View
    }

    override fun detach() {
        mSrcView = null
    }
}
