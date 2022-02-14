package com.ytempest.wanandroid.base.load

import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ytempest.framework.ext.ctx
import com.ytempest.tool.helper.ActivityLauncher
import com.ytempest.tool.state.StateCtrl
import com.ytempest.tool.util.NetUtils
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.widget.jumping.JumpingView

/**
 * @author heqidu
 * @since 21-2-8
 */
class Loader(private val mRootView: ViewGroup) {

    private val mCtrl: StateCtrl<AbsViewState>
    private var mReloadCall: (() -> Unit)? = null

    init {
        val inflater: LayoutInflater = LayoutInflater.from(mRootView.ctx)
        mCtrl = StateCtrl { clazz ->
            val state = AbsViewState.create(clazz)
            state.setup(this, mRootView, inflater)
            state
        }
        mCtrl.start(IdleState::class.java)
    }

    fun showView(@ViewType type: Int) {
        when (type) {
            ViewType.ERR -> mCtrl.moveTo(ErrState::class.java)
            ViewType.LOAD -> mCtrl.moveTo(LoadState::class.java)
            else -> mCtrl.moveTo(IdleState::class.java)
        }
    }

    fun hideAll() {
        mCtrl.setFinish()
    }

    internal fun notifyReloadClick() {
        mReloadCall?.invoke()
    }

    fun setReloadCall(call: (() -> Unit)?) {
        mReloadCall = call
    }
}

private abstract class AbsViewState : StateCtrl.State() {
    protected lateinit var mInflater: LayoutInflater
    protected lateinit var mRootView: ViewGroup
    protected lateinit var mHost: Loader

    internal fun setup(loader: Loader, rootView: ViewGroup, inflater: LayoutInflater) {
        mHost = loader
        mRootView = rootView
        mInflater = inflater
    }

    companion object {
        fun <T : AbsViewState> create(clazz: Class<T>): AbsViewState {
            return when (clazz) {
                ErrState::class.java -> ErrState()
                LoadState::class.java -> LoadState()
                else -> IdleState()
            }
        }
    }
}


private class IdleState : AbsViewState() {

}

private class ErrState : AbsViewState() {
    private lateinit var mErrView: View

    override fun onCreate() {
        super.onCreate()
        mErrView = mInflater.inflate(R.layout.layout_net_error, mRootView, false)
        // 覆盖底层View的点击和滚动
        mErrView.isClickable = true
        mErrView.findViewById<View>(R.id.tv_net_error_retry)
                .setOnClickListener {
                    if (NetUtils.isNetAvailable(mRootView.ctx)) {
                        moveTo(LoadState::class.java)
                        mHost.notifyReloadClick()
                    }
                }
        mErrView.findViewById<View>(R.id.tv_net_error_setting_net)
                .setOnClickListener {
                    ActivityLauncher.startActivity(mRootView.ctx, Intent(Settings.ACTION_SETTINGS))
                }
    }

    override fun onStart(params: Any?) {
        super.onStart(params)
        if (mRootView.indexOfChild(mErrView) == -1) {
            mRootView.addView(mErrView)
        }
        mErrView.visibility = View.VISIBLE
    }

    override fun onStop() {
        mErrView.visibility = View.GONE
    }
}

private class LoadState : AbsViewState() {
    private lateinit var mLoadView: View
    private lateinit var mJumpingView: JumpingView

    override fun onCreate() {
        super.onCreate()
        mLoadView = mInflater.inflate(R.layout.layout_main_load, mRootView, false)
        // 覆盖底层View的点击和滚动
        mLoadView.isClickable = true
        mJumpingView = mLoadView.findViewById(R.id.view_main_load_jumping)
    }

    override fun onStart(params: Any?) {
        super.onStart(params)
        if (mRootView.indexOfChild(mLoadView) == -1) {
            mRootView.addView(mLoadView)
        }
        mJumpingView.start()
        mLoadView.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        mJumpingView.cancel()
        mLoadView.visibility = View.GONE
    }
}