package com.ytempest.wanandroid.noti;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.ytempest.tool.util.SdkUtils;
import com.ytempest.wanandroid.base.WanApp;

/**
 * @author heqidu
 * @since 2020/9/29
 */
public class NotificationHelper {
    private volatile static NotificationHelper sInstance = null;

    public static NotificationHelper getInstance() {
        if (sInstance == null) {
            synchronized (NotificationHelper.class) {
                if (sInstance == null) {
                    sInstance = new NotificationHelper();
                }
            }
        }
        return sInstance;
    }

    private final NotificationManager mManager;
    private final NotificationManagerCompat mCompat;

    private NotificationHelper() {
        mManager = (NotificationManager) WanApp.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        mCompat = NotificationManagerCompat.from(WanApp.getApp());
    }

    public NotificationManager getManager() {
        return mManager;
    }

    public NotificationManagerCompat getCompat() {
        return mCompat;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotiAfterO(int id, Notification.Builder builder, NotificationChannel channel) {
        String channelId = channel.getId();
        Notification notification = builder.setChannelId(channelId).build();
        mManager.createNotificationChannel(channel);
        mCompat.notify(id, notification);
    }


    public void showNoti(int id, Notification.Builder builder) {
        mCompat.notify(id, builder.build());
    }

    @SuppressLint("NewApi")
    public void showSimpleNoti(int id, String channelId, String channelName, Notification.Builder builder) {
        if (SdkUtils.OVER_O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            Notification notification = builder.setChannelId(channelId).build();
            mManager.createNotificationChannel(channel);
            mCompat.notify(id, notification);

        } else {
            mCompat.notify(id, builder.build());
        }
    }
}
