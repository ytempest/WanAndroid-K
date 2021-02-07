package com.ytempest.wanandroid.interactor;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public interface MvpInteractor {

    HttpHelper getHttpHelper();

    DbHelper getDbHelper();

    Configs getConfigs();

}
