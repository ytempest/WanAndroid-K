package com.ytempest.tool.thread;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author heqidu
 * @since 2020/6/20
 */
public class CustomThread {

    private static final String TAG = CustomThread.class.getSimpleName();

    private volatile static CustomThread INSTANCE;

    public static CustomThread getInstance() {
        if (INSTANCE == null) {
            synchronized (CustomThread.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CustomThread();
                }
            }
        }
        return INSTANCE;
    }

    private static final int CORE_SIZE = 0;
    private static final int MAX_SIZE = Integer.MAX_VALUE;
    private static final int KEEP_ALIVE = 60;
    private final ThreadPoolExecutor mThreadExecutor;

    private CustomThread() {
        mThreadExecutor = new ThreadPoolExecutor(CORE_SIZE, MAX_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r, TAG);
                thread.setDaemon(false);
                return thread;
            }
        });
    }

    public ExecutorService getExecutor() {
        return mThreadExecutor;
    }

    public void execute(Runnable r) {
        mThreadExecutor.execute(r);
    }
}
