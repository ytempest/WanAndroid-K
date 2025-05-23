package com.ytempest.tool.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * @author heqidu
 * @since 2020/10/9
 */
public class BroadcastSender {

    public static Sender local(Context context) {
        return LocalSender.getInstance(context);
    }

    public static Sender global(Context context) {
        return GlobalSender.getInstance(context);
    }

    public interface Sender {
        void send(String action);

        void send(Intent intent);

        void register(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter);

        void unregister(@NonNull BroadcastReceiver receiver);
    }

    static class LocalSender implements Sender {

        private volatile static LocalSender sInstance = null;

        public static LocalSender getInstance(Context context) {
            if (sInstance == null) {
                synchronized (LocalSender.class) {
                    if (sInstance == null) {
                        sInstance = new LocalSender(context.getApplicationContext());
                    }
                }
            }
            return sInstance;
        }

        private final LocalBroadcastManager mManager;

        private LocalSender(Context context) {
            mManager = LocalBroadcastManager.getInstance(context);
        }

        @Override
        public void send(String action) {
            send(new Intent(action));
        }

        @Override
        public void send(Intent intent) {
            mManager.sendBroadcast(intent);
        }

        @Override
        public void register(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter) {
            mManager.registerReceiver(receiver, filter);
        }

        @Override
        public void unregister(@NonNull BroadcastReceiver receiver) {
            mManager.unregisterReceiver(receiver);
        }

    }

    static class GlobalSender implements Sender {

        private volatile static GlobalSender sInstance = null;

        public static GlobalSender getInstance(Context context) {
            if (sInstance == null) {
                synchronized (GlobalSender.class) {
                    if (sInstance == null) {
                        sInstance = new GlobalSender(context.getApplicationContext());
                    }
                }
            }
            return sInstance;
        }

        private final Context mContext;

        private GlobalSender(Context context) {
            mContext = context;
        }

        @Override
        public void send(String action) {
            send(new Intent(action));
        }

        @Override
        public void send(Intent intent) {
            mContext.sendBroadcast(intent);
        }

        @Override
        public void register(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter) {
            mContext.registerReceiver(receiver, filter);
        }

        @Override
        public void unregister(@NonNull BroadcastReceiver receiver) {
            mContext.unregisterReceiver(receiver);
        }
    }

}
