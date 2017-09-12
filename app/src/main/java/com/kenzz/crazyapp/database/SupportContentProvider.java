package com.kenzz.crazyapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by huangdefa on 12/09/2017.
 * Version 1.0
 * 自定义ContentProvider,
 * URI=content://com.kenzz.crazyapp.supportprovider/joke
 *
 */

public class SupportContentProvider extends ContentProvider {

    private final static String AUTHORITY="com.kenzz.crazyapp.supportprovider";
    private final static UriMatcher sMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    //joke表的匹配码
    private final static int JOKE_CODE=1;
    static {
        //注册匹配码
        sMatcher.addURI(AUTHORITY,"joke",JOKE_CODE);
    }
    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //TODO: 根据匹配的表查询相应数据库
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int code = sMatcher.match(uri);
        switch (code){
            case JOKE_CODE:
                return "joke";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        getContext().getContentResolver().notifyChange(uri,null);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        getContext().getContentResolver().notifyChange(uri,null);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        //通知改变
        getContext().getContentResolver().notifyChange(uri,null);
        return 0;
    }

}
