package com.kenzz.crazyapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.os.EnvironmentCompat;

import java.io.File;

/**
 * Created by huangdefa on 27/08/2017.
 */

public class DataBaseSupport<T> implements IDataBaseSupport<T> {
    private SQLiteDatabase mSQLiteDatabase;

    @Override
    public void init(Context context) {
        SQLiteDatabase.openOrCreateDatabase(createDatabaseFile("joke"),null);
    }

    private File createDatabaseFile(@NonNull String fileName){
       if(Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED){
           File dirFile=new File(Environment.getExternalStorageDirectory(),"essayJoke");
           if(!dirFile.exists()){
               dirFile.mkdirs();
           }
           return new File(dirFile,fileName+".db");
       }else {
           throw new RuntimeException("ExternalStorage has not be Mounted,can not create db file");
       }
    }

    @Override
    public int insert(T model) {

        return 0;
    }

    @Override
    public boolean delete(T model) {
        return false;
    }
}
