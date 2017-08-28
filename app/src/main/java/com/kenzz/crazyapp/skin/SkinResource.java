package com.kenzz.crazyapp.skin;

import android.content.res.AssetManager;

/**
 * Created by huangdefa on 28/08/2017.
 * 换肤资源类
 */

public class SkinResource {

    private String skinPath;
    private SkinResource mSkinResource;

    public SkinResource(String skinPath) {
        this.skinPath = skinPath;
        try {
            AssetManager assetManager=AssetManager.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
