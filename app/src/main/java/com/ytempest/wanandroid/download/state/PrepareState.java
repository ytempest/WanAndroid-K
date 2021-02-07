package com.ytempest.wanandroid.download.state;

import android.app.Notification;

import com.ytempest.wanandroid.download.NotiClickAction;

/**
 * @author heqidu
 * @since 2020/10/28
 */
public class PrepareState extends AbsDownloadState {

    PrepareState() {
        super(NotiClickAction.ON_PREPARE_CLICK);
    }

    @Override
    protected void onStart(Object params) {
        super.onStart(params);
        Notification notification = mFactory.createStartNoti();
        mChannel.startForeground(notification);
    }

    @Override
    protected void onClickAction() {

    }
}
