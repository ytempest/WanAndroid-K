package com.ytempest.wanandroid.http.observer;

import androidx.annotation.NonNull;

import com.ytempest.wanandroid.http.ErrCode;
import com.ytempest.wanandroid.http.bean.BaseResp;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.observers.DefaultObserver;
import retrofit2.HttpException;

/**
 * @author heqidu
 * @since 2020/7/4
 */
public abstract class BaseObserver<T> extends DefaultObserver<BaseResp<T>> {

    @Override
    public void onNext(BaseResp<T> resp) {
        if (resp == null) {
            onFail(ErrCode.EMPTY_RESP, new DataErrException());
            return;
        }

        if (!resp.isSuccess()) {
            onFail(ErrCode.SRC_ERR, new DataErrException(resp.getErrorMsg()));
            return;
        }

        T data = onHookData(resp.getData());
        if (data == null) {
            onFail(ErrCode.DATA_ERR, new DataErrException(resp.getErrorMsg()));
            return;
        }
        onSuccess(data);
    }

    protected T onHookData(T data) {
        return data;
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException || e instanceof UnknownHostException) {
            onFail(ErrCode.NET_ERR, e);

        } else if (e instanceof SocketTimeoutException) {
            onFail(ErrCode.REQUEST_ERR, e);

        } else {
            onFail(ErrCode.UNKNOWN_ERR, e);
        }
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(@NonNull T t);

    protected abstract void onFail(@ErrCode int code, Throwable e);

}
