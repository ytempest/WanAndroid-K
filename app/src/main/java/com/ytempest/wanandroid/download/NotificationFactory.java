package com.ytempest.wanandroid.download;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.ytempest.tool.util.SdkUtils;
import com.ytempest.wanandroid.R;

/**
 * @author heqidu
 * @since 2020/10/10
 */
public class NotificationFactory {

    private static final String CHANNEL = "noti_app_download";

    private static final int REQ_ON_CONTINUE = 11;
    private static final int REQ_ON_PAUSE = 12;
    private static final int REQ_ON_NOTI = 13;
    private final Service mService;
    private final NotificationManager mManager;
    private Notification.Builder mBuilder;

    NotificationFactory(Service service) {
        mService = service;
        mManager = (NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @SuppressLint("NewApi")
    private Notification createNotification(RemoteViews remoteViews) {
        if (mBuilder == null) {
            String tip = mService.getString(R.string.download_noti);
            mBuilder = new Notification.Builder(mService)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(tip)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setWhen(System.currentTimeMillis());

            if (SdkUtils.OVER_O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL, tip, NotificationManager.IMPORTANCE_DEFAULT);
                mManager.createNotificationChannel(channel);
                mBuilder.setChannelId(CHANNEL);
            }
        }

        if (SdkUtils.OVER_N) {
            mBuilder.setCustomContentView(remoteViews);
        } else {
            mBuilder.setContent(remoteViews);
        }

        return mBuilder.build();
    }

    public Notification createStartNoti() {
        RemoteViews view = new RemoteViews(mService.getPackageName(), R.layout.layout_download_noti);
        view.setViewVisibility(R.id.tv_download_continue, View.GONE);
        view.setViewVisibility(R.id.tv_download_pause, View.VISIBLE);
        PendingIntent broadcast = PendingIntent.getBroadcast(mService, REQ_ON_PAUSE,
                new Intent(NotiClickAction.ON_PAUSE_CLICK), PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.tv_download_pause, broadcast);
        return createNotification(view);
    }

    public Notification createDownloadingNoti(int progress, int maxProgress) {
        RemoteViews view = new RemoteViews(mService.getPackageName(), R.layout.layout_download_noti);
        view.setViewVisibility(R.id.tv_download_continue, View.GONE);
        view.setViewVisibility(R.id.tv_download_pause, View.VISIBLE);
        view.setProgressBar(R.id.view_progress_bar, maxProgress, progress, false);
        PendingIntent broadcast = PendingIntent.getBroadcast(mService, REQ_ON_PAUSE,
                new Intent(NotiClickAction.ON_PAUSE_CLICK), PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.tv_download_pause, broadcast);
        return createNotification(view);
    }

    public Notification createPauseNoti() {
        RemoteViews view = new RemoteViews(mService.getPackageName(), R.layout.layout_download_noti);
        view.setViewVisibility(R.id.tv_download_pause, View.GONE);
        view.setViewVisibility(R.id.tv_download_continue, View.VISIBLE);
        PendingIntent broadcast = PendingIntent.getBroadcast(mService, REQ_ON_CONTINUE,
                new Intent(NotiClickAction.ON_CONTINUE_CLICK), PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.tv_download_continue, broadcast);
        return createNotification(view);
    }

    public Notification createSuccessNoti() {
        RemoteViews view = new RemoteViews(mService.getPackageName(), R.layout.layout_download_noti_success);
        view.setViewVisibility(R.id.tv_download_continue, View.GONE);
        view.setViewVisibility(R.id.tv_download_pause, View.GONE);
        PendingIntent successBroadcast = PendingIntent.getBroadcast(mService, REQ_ON_NOTI,
                new Intent(NotiClickAction.ON_SUCCESS_CLICK), PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.ll_download_root, successBroadcast);
        return createNotification(view);
    }

    public Notification createFailNoti() {
        RemoteViews view = new RemoteViews(mService.getPackageName(), R.layout.layout_download_noti_fail);
        PendingIntent broadcast = PendingIntent.getBroadcast(mService, REQ_ON_NOTI,
                new Intent(NotiClickAction.ON_FAIL_CLICK), PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.ll_download_root, broadcast);
        return createNotification(view);
    }
}
