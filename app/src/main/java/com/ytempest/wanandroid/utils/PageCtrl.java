package com.ytempest.wanandroid.utils;

import androidx.annotation.IntDef;

import com.ytempest.tool.util.LogUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.functions.Predicate;

/**
 * @author heqidu
 * @since 2020/12/25
 * 文章页面页码状态控制器
 */
public final class PageCtrl {

    private static final String TAG = PageCtrl.class.getSimpleName();

    private final AtomicInteger version = new AtomicInteger();
    private int nextPage = 0;
    private boolean isRequesting;
    private int state;

    public int getVersion() {
        return version.get();
    }

    public boolean isSameVersion(int version) {
        return this.version.get() == version;
    }

    public int getNextPage() {
        return nextPage;
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    @State
    public int getState() {
        return state;
    }

    public void moveTo(@State int state) {
        this.state = state;
        switch (state) {
            case State.REFRESH:
                isRequesting = true;
                nextPage = 0;
                version.incrementAndGet();
                break;

            case State.LOAD_MORE:
                isRequesting = true;
                break;

            case State.SUCCESS:
                isRequesting = false;
                nextPage++;
                break;

            case State.FAIL:
                isRequesting = false;
                break;

            default:
                break;
        }
    }

    @IntDef({State.REFRESH, State.LOAD_MORE, State.SUCCESS, State.FAIL,})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
        int REFRESH = 1;
        int LOAD_MORE = 2;
        int SUCCESS = 3;
        int FAIL = 4;
    }

    /*Ext*/

    /**
     * 检查是否过滤掉由于网络问题引起的，请求不同页码数据时的旧数据问题
     */
    public <T> Predicate<T> filterDirtyData() {
        final int lastVersion = getVersion();
        return data -> {
            LogUtils.d(TAG, String.format("filterDirtyData: 该次操作的页码版本: %d, 当前页码版本: %d, 该次操作的数据是否有效： %s", lastVersion, version.get(), isSameVersion(lastVersion)));
            return isSameVersion(lastVersion);
        };
    }
}
