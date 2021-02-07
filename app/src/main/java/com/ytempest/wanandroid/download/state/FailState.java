package com.ytempest.wanandroid.download.state;

import android.app.Notification;

import com.ytempest.tool.util.LogUtils;
import com.ytempest.wanandroid.download.NotiClickAction;

/**
 * @author heqidu
 * @since 2020/10/28
 */
public class FailState extends AbsDownloadState {

    private static final String TAG = FailState.class.getSimpleName();

    FailState() {
        super(NotiClickAction.ON_FAIL_CLICK);
    }

    @Override
    protected void onStart(Object params) {
        super.onStart(params);
        Notification notification = mFactory.createFailNoti();
        mChannel.notifyNotification(notification);
    }

    @Override
    protected void onClickAction() {
        LogUtils.d(TAG, "onClickAction: ");
    }
}
