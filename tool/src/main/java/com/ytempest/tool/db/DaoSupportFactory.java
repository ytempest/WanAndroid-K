package com.ytempest.tool.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.ytempest.tool.util.FileUtils;

import java.io.File;

/**
 * @author heqidu
 * @since 2020/2/27
 */
public class DaoSupportFactory {

    private static final String DB_DIR = "database";

    private SQLiteDatabase mSqLiteDatabase;

    private final Context mContext;
    private final String mDbName;

    private DaoSupportFactory(Builder build) {
        this.mContext = build.context;
        this.mDbName = build.dbName;

        initDatabase();
    }

    private void initDatabase() {
        File dbRoot = new File(mContext.getFilesDir(), DB_DIR);
        FileUtils.createDir(dbRoot);
        File dbFile = new File(dbRoot, mDbName);

        // 打开或者创建一个数据库
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public <T> IDaoSupport<T> getDao(Class<T> clazz) {
        IDaoSupport<T> daoSupport = new DaoSupport<>();
        // 初始化数据库引擎以及clazz表
        daoSupport.init(mSqLiteDatabase, clazz);
        return daoSupport;
    }

    public static class Builder {
        private Context context;
        private String dbName;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setDbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public DaoSupportFactory build() {
            checkParams();
            return new DaoSupportFactory(this);
        }

        private void checkParams() {
            if (TextUtils.isEmpty(dbName)) {
                throw new IllegalStateException("Please set database name");
            }
        }
    }
}
