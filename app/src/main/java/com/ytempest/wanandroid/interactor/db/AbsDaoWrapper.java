package com.ytempest.wanandroid.interactor.db;

import com.ytempest.wanandroid.interactor.db.bean.DaoSession;

import org.greenrobot.greendao.AbstractDao;

/**
 * @author heqidu
 * @since 2020/11/19
 */
public abstract class AbsDaoWrapper<Dao extends AbstractDao> {

    protected DaoSession mDaoSession;
    protected Dao mDao;

    public void attach(DaoSession session) {
        mDaoSession = session;
        mDao = onGetDao(session);
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public Dao getSrcDao() {
        return mDao;
    }

    protected abstract Dao onGetDao(DaoSession session);
}
