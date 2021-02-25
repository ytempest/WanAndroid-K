package com.ytempest.wanandroid.download;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationManagerCompat;

import com.ytempest.tool.receiver.BaseReceiver;
import com.ytempest.tool.state.StateCtrl;
import com.ytempest.wanandroid.di.module.http.OkHttpManager;
import com.ytempest.wanandroid.download.state.AbsDownloadState;
import com.ytempest.wanandroid.download.state.DownloadingState;
import com.ytempest.wanandroid.download.state.FailState;
import com.ytempest.wanandroid.download.state.PrepareState;
import com.ytempest.wanandroid.download.state.SuccessState;
import com.ytempest.wanandroid.interactor.db.DbController;
import com.ytempest.wanandroid.interactor.db.bean.DownloadRecord;

import java.io.File;

/**
 * @author heqidu
 * @since 2020/9/29
 */
public class DownloadChannel {

    private static final int NOTIFICATION_DOWNLOAD_ID = 11;

    private final Service mService;
    private final NotificationManagerCompat mCompat;
    private final DownloadUnit mDownloadUnit;
    private final BaseReceiver mReceiver;
    private final StateCtrl<AbsDownloadState> mStateCtrl;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public DownloadChannel(Service service) {
        mService = service;
        mCompat = NotificationManagerCompat.from(service);
        mDownloadUnit = new DownloadUnit(OkHttpManager.Companion.getInstance().getClient())
                .setDownloadListener(new DownloadUnit.DownloadListener() {

                    private static final int TIME_NOTI_DELAY = 300;

                    @Override
                    public void onProgress(long saveLen, long contentLength) {
                        DownloadRecord record = new DownloadRecord(null, mDownloadUnit.getUrl(),
                                mDownloadUnit.getSaveFile().getAbsolutePath(), saveLen);
                        DbController.getDownloadRecordDao().update(record);
                        mHandler.post(() -> mStateCtrl.getCurrent().onUpdateProgress(1F * saveLen / contentLength));
                    }

                    @Override
                    public void onSuccess(File file) {
                        // 延迟300毫秒，以确保该通知正常刷新
                        mHandler.postDelayed(() -> mStateCtrl.moveTo(SuccessState.class, file), TIME_NOTI_DELAY);
                    }

                    @Override
                    public void onFail(Throwable throwable) {
                        mHandler.postDelayed(() -> mStateCtrl.moveTo(FailState.class, throwable), TIME_NOTI_DELAY);
                    }
                });

        // 通知点击广播
        IntentFilter filter = BaseReceiver.crateFilter(NotiClickAction.ACTIONS);
        mReceiver = new BaseReceiver(filter, false) {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                mStateCtrl.getCurrent().receiveClickAction(action);
            }
        };
        mReceiver.register(service);

        // 下载状态控制
        final NotificationFactory factory = new NotificationFactory(service);
        mStateCtrl = new StateCtrl<>(clazz -> {
            AbsDownloadState state = AbsDownloadState.create(clazz);
            state.setup(this, factory);
            return state;
        });
        mStateCtrl.start(PrepareState.class);
    }

    public void update(String url, File file) {
        mDownloadUnit.setup(url, file);
    }

    public void startDownload() {
        mStateCtrl.moveTo(DownloadingState.class);
    }

    public void stopDownload() {
        mDownloadUnit.cancelDownload();
    }

    public void resumeDownload() {
        String url = mDownloadUnit.getUrl();
        String savePath = mDownloadUnit.getSaveFile().getAbsolutePath();
        long seek = DbController.getDownloadRecordDao().getSeek(url, savePath);
        mDownloadUnit.startDownload(seek);
    }

    public void startForeground(Notification notification) {
        mService.startForeground(NOTIFICATION_DOWNLOAD_ID, notification);
    }

    public void notifyNotification(Notification notification) {
        mCompat.notify(NOTIFICATION_DOWNLOAD_ID, notification);
    }

    public void notifyNotificationDelay(Notification notification, long delay) {
        mHandler.postDelayed(() -> mCompat.notify(NOTIFICATION_DOWNLOAD_ID, notification), delay);
    }

    public void destroy() {
        stopDownload();
        mHandler.removeCallbacksAndMessages(null);
        mReceiver.unregister(mService);
    }
}
