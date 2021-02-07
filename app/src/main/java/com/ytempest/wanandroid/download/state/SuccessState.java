package com.ytempest.wanandroid.download.state;

import android.app.Notification;

import com.ytempest.tool.util.LogUtils;
import com.ytempest.wanandroid.download.NotiClickAction;

/**
 * @author heqidu
 * @since 2020/10/28
 */
public class SuccessState extends AbsDownloadState {

    private static final String TAG = SuccessState.class.getSimpleName();

    SuccessState() {
        super(NotiClickAction.ON_SUCCESS_CLICK);
    }

    @Override
    protected void onStart(Object params) {
        super.onStart(params);
        Notification notification = mFactory.createSuccessNoti();
        mChannel.notifyNotification(notification);
    }

    @Override
    protected void onClickAction() {
        LogUtils.d(TAG, "onClickAction: ");
    }
}
