package com.kenzz.crazyapp.database;

import android.content.Context;

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

}
