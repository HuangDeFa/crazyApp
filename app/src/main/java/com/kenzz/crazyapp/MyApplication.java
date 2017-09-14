package com.kenzz.crazyapp;

import android.app.Application;

import com.kenzz.crazyapp.database.DataBaseSupportFactory;
import com.kenzz.crazyapp.skin.SkinManager;
import com.kenzz.crazyapp.utils.ExceptionHelper;

/**
 * Created by huangdefa on 26/08/2017.
 * 自定义Application
 *
 */

public class MyApplication extends Application {

    //方便获取Application对象
    private static MyApplication mInstance;
    public static MyApplication getmInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        ExceptionHelper.getExceptionHelper().init(this);
       // DataBaseSupportFactory.getFactory().getDataBaseSupport().init(this);
        SkinManager.getInstance().init(this);
    }
}
