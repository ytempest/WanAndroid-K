package com.ytempest.wanandroid.interactor.db.dao;

import com.ytempest.wanandroid.interactor.db.AbsDaoWrapper;
import com.ytempest.wanandroid.interactor.db.bean.DaoSession;
import com.ytempest.wanandroid.interactor.db.bean.DownloadRecord;
import com.ytempest.wanandroid.interactor.db.bean.DownloadRecordDao;


/**
 * @author heqidu
 * @since 2020/11/19
 */
public class DownloadRecordDaoWrapper extends AbsDaoWrapper<DownloadRecordDao> {

    @Override
    protected DownloadRecordDao onGetDao(DaoSession session) {
        return session.getDownloadRecordDao();
    }

    public long getSeek(String url, String savePath) {
        DownloadRecord record = mDao.queryBuilder()
                .where(DownloadRecordDao.Properties.Url.eq(url), DownloadRecordDao.Properties.SavePath.eq(savePath))
                .build()
                .unique();
        return record != null ? record.getLastSeek() : 0;
    }

    public void update(DownloadRecord record) {
        DownloadRecord old = mDao.queryBuilder()
                .where(DownloadRecordDao.Properties.Url.eq(record.getUrl()), DownloadRecordDao.Properties.SavePath.eq(record.getSavePath()))
                .build().unique();

        if (old != null) {
            record.setRecordId(old.getRecordId());
            mDao.update(record);
        } else {
            mDao.insert(record);
        }
    }


}
