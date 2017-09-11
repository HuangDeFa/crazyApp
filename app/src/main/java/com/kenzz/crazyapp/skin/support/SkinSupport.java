package com.kenzz.crazyapp.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.kenzz.crazyapp.skin.attr.SkinAttr;
import com.kenzz.crazyapp.skin.attr.SkinType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangdefa on 28/08/2017.
 * 换肤具体操作支持类
 */

public class SkinSupport {
private static final String TAG=SkinSupport.class.getSimpleName();

    public static List<SkinAttr> getSkinViewAttrs(Context context, AttributeSet attrs){
        List<SkinAttr> skinAttrs=new ArrayList<>();
        int attrCount=attrs.getAttributeCount();
        for(int index=0;index<attrCount;index++){
            String name= attrs.getAttributeName(index);
            SkinType skinType= getSkinTypeByName(name);
            Log.d(TAG,"AttrName -->"+name);
            if(skinType!=null){
                String value = attrs.getAttributeValue(index);
                String resName = getResName(context,value);
                if(TextUtils.isEmpty(resName)){
                    continue;
                }
                SkinAttr skinAttr=new SkinAttr(skinType,resName);
                skinAttrs.add(skinAttr);
            }
        }
        return skinAttrs;
    }

    /**
     * 将资源 ID @1232424 转换为 R.xxx.resName 中的resName
     * 反过来就可以通过
     * @param context
     * @param value
     * @return
     */
    private static String getResName(Context context, String value) {
        if(value.startsWith("@")){
            value=value.substring(1);
            int resId=Integer.parseInt(value);
            if(resId>0) {
                return context.getResources().getResourceEntryName(resId);
            }
        }
        return null;
    }

    private static SkinType getSkinTypeByName(String name) {
        SkinType[] skinTypes = SkinType.values();
        for (SkinType skinType : skinTypes) {
            if(skinType.getResName().equals(name)){
                return skinType;
            }
        }
        return null;
    }
}
