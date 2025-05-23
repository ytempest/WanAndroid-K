package com.ytempest.tool.base.mvp;

import android.app.Application;
import android.content.Context;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public interface IContract {

    Context getContext();

    Application getApp();
}
