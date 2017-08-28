package com.kenzz.crazyapp.skin.attr;

import android.view.View;

/**
 * Created by huangdefa on 28/08/2017.
 */

public class SkinAttr {
     private SkinType mSkinType;
     private String mResource;

    public SkinAttr(SkinType skinType, String resource) {
        mSkinType = skinType;
        mResource = resource;
    }

    public void skin(View view) {
        mSkinType.skin(view,mResource);
    }
}
