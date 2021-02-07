package com.ytempest.wanandroid.download;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.ytempest.tool.util.DataUtils;

import java.io.File;

/**
 * @author heqidu
 * @since 2020/9/29
 */
public class DownloadService extends Service {

    private static final String TAG = DownloadService.class.getSimpleName();

    private static final String KEY_DOWNLOAD_URL = "download_url";
    private static final String KEY_SAVE_PATH = "save_path";

    public static void start(Context context, String url, String savePath) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(KEY_DOWNLOAD_URL, url);
        intent.putExtra(KEY_SAVE_PATH, savePath);
        ContextCompat.startForegroundService(context, intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private DownloadChannel mChannel;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String url = intent.getStringExtra(KEY_DOWNLOAD_URL);
        String savePath = intent.getStringExtra(KEY_SAVE_PATH);
        if (DataUtils.isNull(url, savePath)) {
            stopSelf();
            return START_STICKY;
        }
        if (mChannel == null) {
            mChannel = new DownloadChannel(this);
        }
        mChannel.update(url, new File(savePath));
        mChannel.startDownload();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChannel != null) {
            mChannel.destroy();
        }
    }
}
