package com.ytempest.wanandroid.download.state;

import android.app.Notification;

import com.ytempest.wanandroid.download.NotiClickAction;

/**
 * @author heqidu
 * @since 2020/10/28
 */
public class PauseState extends AbsDownloadState {

    private static final int NOTI_DELAY = 300;

    PauseState() {
        super(NotiClickAction.ON_CONTINUE_CLICK);
    }

    @Override
    protected void onStart(Object params) {
        super.onStart(params);
        mChannel.stopDownload();
        Notification notification = mFactory.createPauseNoti();
        mChannel.notifyNotificationDelay(notification, NOTI_DELAY);
    }

    @Override
    protected void onClickAction() {
        moveTo(DownloadingState.class);
    }
}
