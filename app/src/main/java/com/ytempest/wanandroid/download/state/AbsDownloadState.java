package com.ytempest.wanandroid.download.state;

import android.app.Service;

import com.ytempest.tool.state.StateCtrl;
import com.ytempest.tool.util.Utils;
import com.ytempest.wanandroid.download.DownloadChannel;
import com.ytempest.wanandroid.download.NotiClickAction;
import com.ytempest.wanandroid.download.NotificationFactory;

/**
 * @author heqidu
 * @since 2020/10/28
 */
public abstract class AbsDownloadState extends StateCtrl.State {

    private final String mClickAction;
    protected DownloadChannel mChannel;
    protected NotificationFactory mFactory;


    public AbsDownloadState(@NotiClickAction String clickAction) {
        mClickAction = clickAction;
    }

    public void setup(DownloadChannel channel, NotificationFactory factory) {
        mChannel = channel;
        mFactory = factory;
    }

    public void onUpdateProgress(float percent) {

    }

    public void receiveClickAction(String action) {
        if (Utils.isQuickClick()) return;
        if (mClickAction.equals(action)) {
            onClickAction();
        }
    }

    protected abstract void onClickAction();

    public static AbsDownloadState create(Class<? extends AbsDownloadState> clazz) {
        if (clazz == DownloadingState.class) {
            return new DownloadingState();

        } else if (clazz == PauseState.class) {
            return new PauseState();

        } else if (clazz == SuccessState.class) {
            return new SuccessState();

        } else if (clazz == FailState.class) {
            return new FailState();

        } else {
            return new PrepareState();
        }
    }

}
