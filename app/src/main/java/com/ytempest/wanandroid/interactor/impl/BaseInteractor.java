package com.ytempest.wanandroid.interactor.impl;

import com.ytempest.wanandroid.interactor.DbHelper;
import com.ytempest.wanandroid.interactor.HttpHelper;
import com.ytempest.wanandroid.interactor.MvpInteractor;
import com.ytempest.wanandroid.interactor.Configs;

import javax.inject.Inject;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public class BaseInteractor implements MvpInteractor {
    private final HttpHelper mHttpHelper;
    private final DbHelper mDbHelper;
    private final Configs mConfigs;

    @Inject
    public BaseInteractor(HttpHelper httpHelper, DbHelper dbHelper, Configs configs) {
        mHttpHelper = httpHelper;
        mDbHelper = dbHelper;
        mConfigs = configs;
    }

    @Override
    public HttpHelper getHttpHelper() {
        return mHttpHelper;
    }

    @Override
    public DbHelper getDbHelper() {
        return mDbHelper;
    }

    @Override
    public Configs getConfigs() {
        return mConfigs;
    }
}
