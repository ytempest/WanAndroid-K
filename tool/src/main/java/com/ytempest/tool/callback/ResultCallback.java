package com.ytempest.tool.callback;

import androidx.annotation.Nullable;

/**
 * @author heqidu
 * @since 2020/3/2
 */
public interface ResultCallback<Param, Result> {
    Result onCall(@Nullable Param param);
}
