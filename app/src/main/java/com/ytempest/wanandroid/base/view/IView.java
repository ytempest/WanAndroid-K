package com.ytempest.wanandroid.base.view;

import androidx.annotation.StringRes;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public interface IView {
    void showToast(String msg);

    void showToast(@StringRes int textId);

    void showLoading();

    void stopLoading();
}
