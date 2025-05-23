package com.ytempest.tool.db;

import android.database.sqlite.SQLiteDatabase;

import com.ytempest.tool.db.curd.QuerySupport;

import java.util.List;

/**
 * @author ytempest
 * @since 2017/3/5
 */
public interface IDaoSupport<T> {

    /**
     * 初始化数据库引擎
     *
     * @param sqLiteDatabase 数据库实例
     * @param clazz          进行操作的表的Class对象
     */
    void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz);

    long insert(T t);

    void insert(List<T> list);

    QuerySupport<T> getQuerySupport();

    int delete(String whereClause, String... whereArgs);

    int update(T obj, String whereClause, String... whereArgs);
}
