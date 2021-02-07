package com.ytempest.wanandroid.base.load;

import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ytempest.tool.callback.Callback;
import com.ytempest.tool.callback.SimpleCallback;
import com.ytempest.tool.helper.ActivityLauncher;
import com.ytempest.tool.state.StateCtrl;
import com.ytempest.tool.util.NetUtils;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.widget.jumping.JumpingView;

/**
 * @author heqidu
 * @since 2020/12/28
 */
public class Loader {

    private final ViewGroup mRootView;
    private final StateCtrl<AbsViewState> mCtrl;
    private Callback<Void> mReloadCall;

    public Loader(ViewGroup rootView) {
        mRootView = rootView;
        final LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        mCtrl = new StateCtrl<>(clazz -> {
            AbsViewState state = AbsViewState.create(clazz);
            state.setup(this, mRootView, inflater);
            return state;
        });
        mCtrl.start(IdleState.class);
    }

    public void showView(@ViewType int type) {
        switch (type) {
            case ViewType.ERR:
                mCtrl.moveTo(ErrState.class);
                break;
            case ViewType.LOAD:
                mCtrl.moveTo(LoadState.class);
                break;
            default:
                mCtrl.moveTo(IdleState.class);
                break;
        }
    }

    public void hideAll() {
        mCtrl.setFinish();
    }

    private void notifyReloadClick() {
        SimpleCallback.call(mReloadCall);
    }

    public void setReloadCall(Callback<Void> reloadCall) {
        mReloadCall = reloadCall;
    }

    private static class AbsViewState extends StateCtrl.State {

        protected LayoutInflater mInflater;
        protected ViewGroup mRootView;
        protected Loader mHost;

        void setup(Loader loader, ViewGroup rootView, LayoutInflater inflater) {
            mHost = loader;
            mRootView = rootView;
            mInflater = inflater;
        }

        public static AbsViewState create(Class<? extends AbsViewState> clazz) {
            if (clazz == ErrState.class) {
                return new ErrState();

            } else if (clazz == LoadState.class) {
                return new LoadState();

            } else {
                return new IdleState();
            }
        }
    }

    private static class IdleState extends AbsViewState {
    }

    private static class ErrState extends AbsViewState {

        private View mErrView;

        @Override
        protected void onCreate() {
            super.onCreate();
            mErrView = mInflater.inflate(R.layout.layout_net_error, mRootView, false);
            // 覆盖底层View的点击和滚动
            mErrView.setClickable(true);
            mErrView.findViewById(R.id.tv_net_error_retry)
                    .setOnClickListener(v -> {
                        if (NetUtils.isNetAvailable(mRootView.getContext())) {
                            moveTo(LoadState.class);
                            mHost.notifyReloadClick();
                        }
                    });
            mErrView.findViewById(R.id.tv_net_error_setting_net)
                    .setOnClickListener(v -> ActivityLauncher.startActivity(mRootView.getContext(), new Intent(Settings.ACTION_SETTINGS)));
        }

        @Override
        protected void onStart(Object params) {
            super.onStart(params);
            if (mRootView.indexOfChild(mErrView) == -1) {
                mRootView.addView(mErrView);
            }
            mErrView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onStop() {
            super.onStop();
            mErrView.setVisibility(View.GONE);
        }
    }

    private static class LoadState extends AbsViewState {

        private View mLoadView;
        private JumpingView mJumpingView;

        @Override
        protected void onCreate() {
            super.onCreate();
            mLoadView = mInflater.inflate(R.layout.layout_main_load, mRootView, false);
            // 覆盖底层View的点击和滚动
            mLoadView.setClickable(true);
            mJumpingView = mLoadView.findViewById(R.id.view_main_load_jumping);
        }

        @Override
        protected void onStart(Object params) {
            super.onStart(params);
            if (mRootView.indexOfChild(mLoadView) == -1) {
                mRootView.addView(mLoadView);
            }
            mJumpingView.start();
            mLoadView.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onStop() {
            super.onStop();
            mJumpingView.cancel();
            mLoadView.setVisibility(View.GONE);
        }
    }
}
