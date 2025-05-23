package com.ytempest.tool.callback;

import androidx.annotation.Nullable;

/**
 * @author heqidu
 * @since 2020/2/22
 */
public interface Callback<Param> {
    void onCall(@Nullable Param param);
}
