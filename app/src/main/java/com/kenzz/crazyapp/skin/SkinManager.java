package com.kenzz.crazyapp.skin;

import android.content.Context;

/**
 * Created by huangdefa on 28/08/2017.
 *  换肤管理
 */

public class SkinManager {

    private Context mContext;
    private SkinResource mSkinResource;

    private SkinManager(){}
    private static SkinManager sSkinManager;
    static {
        sSkinManager=new SkinManager();
    }

    public static SkinManager getInstance(){
        return sSkinManager;
    }

    public void init(Context context){
        mContext=context.getApplicationContext();
    }

    public void loadSkin(String skinPath){

    }

    public void restoreSkin(){

    }
}
