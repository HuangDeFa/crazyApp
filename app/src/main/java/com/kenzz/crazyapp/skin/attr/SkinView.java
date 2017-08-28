package com.kenzz.crazyapp.skin.attr;

import android.view.View;

import java.util.List;

/**
 * Created by huangdefa on 28/08/2017.
 */

public class SkinView {
    private View mView;
    private List<SkinAttr> mSkinAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        mView = view;
        mSkinAttrs = skinAttrs;
    }

    public void skin(){
        for (SkinAttr skinAttr : mSkinAttrs) {
            skinAttr.skin(mView);
        }
    }
}
