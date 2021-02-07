package com.ytempest.wanandroid.http.observer;

import androidx.annotation.NonNull;

import com.ytempest.tool.callback.Callback;
import com.ytempest.tool.callback.ParamsCallback;
import com.ytempest.tool.callback.SimpleCallback;

/**
 * @author heqidu
 * @since 2020/8/13
 */
public class OptionalObserver<T> extends BaseObserver<T> {

    @Override
    protected void onStart() {
        super.onStart();
        SimpleCallback.call(mOnStartCall);
    }

    @Override
    protected void onSuccess(@NonNull T t) {
        SimpleCallback.call(mOnCompletedCall);
        SimpleCallback.call(mOnSuccessCall, t);
    }

    @Override
    protected void onFail(int code, Throwable e) {
        SimpleCallback.call(mOnCompletedCall);
        SimpleCallback.call(mOnFailCall, code, e);
    }

    private Callback<Void> mOnStartCall;
    private Callback<T> mOnSuccessCall;
    private ParamsCallback<Integer, Throwable> mOnFailCall;
    private Callback<Void> mOnCompletedCall;

    public OptionalObserver<T> doOnStart(Callback<Void> onStartCall) {
        mOnStartCall = onStartCall;
        return this;
    }

    public OptionalObserver<T> doOnSuccess(Callback<T> onSuccessCall) {
        mOnSuccessCall = onSuccessCall;
        return this;
    }

    public OptionalObserver<T> doOnFail(ParamsCallback<Integer, Throwable> onFailCall) {
        mOnFailCall = onFailCall;
        return this;
    }

    public OptionalObserver<T> doOnCompleted(Callback<Void> completedCall) {
        mOnCompletedCall = completedCall;
        return this;
    }
}
