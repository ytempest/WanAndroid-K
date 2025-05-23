package com.ytempest.tool.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ytempest.tool.util.DataUtils;

/**
 * @author heqidu
 * @since 2020/10/9
 */
public abstract class BaseReceiver extends BroadcastReceiver {

    private final IntentFilter mFilter;
    private final boolean isLocal;

    public BaseReceiver(IntentFilter filter) {
        this(filter, true);
    }

    public BaseReceiver(IntentFilter filter, boolean local) {
        this.mFilter = filter;
        this.isLocal = local;
    }

    public void register(Context context) {
        BroadcastSender.Sender sender = isLocal ? BroadcastSender.local(context) : BroadcastSender.global(context);
        sender.register(this, mFilter);
    }

    public void unregister(Context context) {
        BroadcastSender.Sender sender = isLocal ? BroadcastSender.local(context) : BroadcastSender.global(context);
        sender.unregister(this);
    }

    public void send(Context context, String action) {
        BroadcastSender.Sender sender = isLocal ? BroadcastSender.local(context) : BroadcastSender.global(context);
        sender.send(action);
    }

    public void send(Context context, Intent intent) {
        BroadcastSender.Sender sender = isLocal ? BroadcastSender.local(context) : BroadcastSender.global(context);
        sender.send(intent);
    }

    public static IntentFilter crateFilter(String... actions) {
        IntentFilter filter = new IntentFilter();
        for (int i = 0, size = DataUtils.getSize(actions); i < size; i++) {
            filter.addAction(actions[i]);
        }
        return filter;
    }
}
