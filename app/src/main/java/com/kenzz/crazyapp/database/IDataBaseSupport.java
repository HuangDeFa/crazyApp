package com.kenzz.crazyapp.database;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;

/**
 * Created by huangdefa on 27/08/2017.
 *  数据库支持
 */

public interface IDataBaseSupport<T> {

    void init(Context context);

    //像数据库插入数据
    int insert(T model);

    //删除数据
    boolean delete(T model);

    //根据Id删除数据
    boolean delete(Class<T> clazz,int id);

    boolean delete(Class<T> clazz,int[] ids);

    //更新数据
    int update(T model);

    //查询数据
    List<T> queryAll(Class<T> clazz);

    //根据Id查询
    T queryById(Class<T> clazz,int id);

    //
    List<T> query(Class<T> clazz, ContentValues contentValues);
}
