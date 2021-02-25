package com.ytempest.wanandroid.interactor.db;

import com.ytempest.tool.state.InstanceCache;
import com.ytempest.wanandroid.base.WanApp;
import com.ytempest.wanandroid.interactor.db.bean.DaoMaster;
import com.ytempest.wanandroid.interactor.db.bean.DaoSession;
import com.ytempest.wanandroid.interactor.db.dao.DownloadRecordDaoWrapper;

/**
 * @author heqidu
 * @since 2020/7/6
 */
public class DbController {

    private volatile static DbController sInstance = null;

    public static DbController getInstance() {
        if (sInstance == null) {
            synchronized (DbController.class) {
                if (sInstance == null) {
                    sInstance = new DbController();
                }
            }
        }
        return sInstance;
    }

    private static final String DB_NAME = "wan.db";
    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private DaoSession mDaoSession;
    private InstanceCache<AbsDaoWrapper> mDaoCache;

    private DbController() {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(WanApp.instance, DB_NAME, null);
        DaoMaster daoMaster = new DaoMaster(mDevOpenHelper.getWritableDatabase());
        mDaoSession = daoMaster.newSession();

        mDaoCache = new InstanceCache<>();
        mDaoCache.setInitListener(wrapperDao -> wrapperDao.attach(mDaoSession));
    }

    private <D extends AbsDaoWrapper> D getDao(Class<D> dao) {
        return mDaoCache.get(dao);
    }

    public static DownloadRecordDaoWrapper getDownloadRecordDao() {
        return DbController.getInstance().getDao(DownloadRecordDaoWrapper.class);
    }

}
