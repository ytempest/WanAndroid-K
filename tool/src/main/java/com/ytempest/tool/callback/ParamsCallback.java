package com.ytempest.tool.callback;

import androidx.annotation.Nullable;

/**
 * @author heqidu
 * @since 2020/2/27
 */
public interface ParamsCallback<First, Second> {
    void onCall(@Nullable First first, @Nullable Second second);
}
