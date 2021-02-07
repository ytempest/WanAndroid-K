package com.ytempest.wanandroid.http.observer;

import android.annotation.SuppressLint;
import androidx.annotation.IntDef;

import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.base.view.IView;
import com.ytempest.wanandroid.http.ErrCode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author heqidu
 * @since 2020/7/4
 */
public abstract class HandlerObserver<T> extends BaseObserver<T> {

    public static final int SHOW_ERR_MSG = 0x000001;
    public static final int SHOW_LOADING = 0x000010;

    private final IView mView;
    private final int mFlags;

    @SuppressLint("WrongConstant")
    public HandlerObserver(IView view) {
        this(view, 0);
    }

    public HandlerObserver(IView view, @Flags int flags) {
        this.mView = view;
        this.mFlags = flags;
    }

    private boolean enable(@Flags int flags) {
        return (mFlags & flags) != 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (enable(SHOW_LOADING)) {
            mView.showLoading();
        }
    }

    @Override
    public void onComplete() {
        super.onComplete();
        if (enable(SHOW_LOADING)) {
            mView.stopLoading();
        }
    }

    @Override
    protected void onFail(@ErrCode int code, Throwable e) {
        if (mView == null) return;

        if (enable(SHOW_LOADING)) {
            mView.stopLoading();
        }

        if (enable(SHOW_ERR_MSG)) {
            switch (code) {
                case ErrCode.NET_ERR:
                    mView.showToast(R.string.net_err);
                    break;
                case ErrCode.EMPTY_RESP:
                    mView.showToast(R.string.get_data_fail);
                    break;
                case ErrCode.DATA_ERR:
                    mView.showToast(R.string.request_fail);
                    break;
                case ErrCode.UNKNOWN_ERR:
                    mView.showToast(R.string.unknown_err);
                    break;
            }
        }
    }

    @IntDef({
            SHOW_ERR_MSG,
            SHOW_LOADING
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }
}
