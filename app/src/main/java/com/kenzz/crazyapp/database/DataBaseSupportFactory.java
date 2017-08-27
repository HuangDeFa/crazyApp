package com.kenzz.crazyapp.database;

/**
 * Created by huangdefa on 27/08/2017.
 */

public class DataBaseSupportFactory<T> {
    private static DataBaseSupportFactory sFactory;
    private DataBaseSupportFactory() {
        mDataBaseSupport=new DataBaseSupport<>();
    }
    public static DataBaseSupportFactory getFactory(){
        if(sFactory==null){
            synchronized (DataBaseSupportFactory.class){
                if(sFactory==null){
                    sFactory=new DataBaseSupportFactory();
                }
            }
        }
        return sFactory;
    }

    private IDataBaseSupport<T> mDataBaseSupport;

    public IDataBaseSupport<T> getDataBaseSupport() {
        return mDataBaseSupport;
    }
}
