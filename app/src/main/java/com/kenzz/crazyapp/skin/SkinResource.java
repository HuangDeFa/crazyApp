package com.kenzz.crazyapp.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Method;

/**
 * Created by huangdefa on 28/08/2017.
 * 换肤资源类
 */

public class SkinResource {
    private Resources mSkinResource;
    private String mpackageName;


    public SkinResource(Context context, String skinPath) {
        try {
            Resources superRes=context.getResources();
            AssetManager assetManager=AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager,skinPath);
            mSkinResource=new Resources(assetManager,superRes.getDisplayMetrics(),superRes.getConfiguration());

             mpackageName = context.getPackageManager().
                    getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据名字获取图片
     * @param name
     * @return
     */
    public Drawable getDrawableByName(String name){
        try {
            int resId = mSkinResource.getIdentifier(name, "drawable", mpackageName);
            if (resId > 0) {
                return mSkinResource.getDrawable(resId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据名字获取颜色
     * @param name
     * @return
     */
    public ColorStateList getColorByName(String name){
        try {
            int resId = mSkinResource.getIdentifier(name, "color", mpackageName);
            if (resId > 0) {
                return mSkinResource.getColorStateList(resId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
