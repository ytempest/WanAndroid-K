package com.ytempest.tool.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;

/**
 * @author heqidu
 * @since 2020/6/20
 */
public class Threads {

    /*main*/

    private static final Handler mMainHandler = new Handler(Looper.getMainLooper());

    public static void runOnMain(Runnable task) {
        if (isMainThread()) {
            task.run();
        } else {
            mMainHandler.post(task);
        }
    }

    public static void runOnMain(Runnable task, long delay) {
        mMainHandler.postDelayed(task, delay);
    }

    public static void removeCallback(Runnable task) {
        mMainHandler.removeCallbacks(task);
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    /*io*/

    public static ExecutorService getExecutor() {
        return CustomThread.getInstance().getExecutor();
    }

    public static void execute(Runnable task) {
        getExecutor().execute(task);
    }
}
