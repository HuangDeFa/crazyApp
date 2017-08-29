package com.kenzz.crazyapp.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenzz.crazyapp.skin.SkinManager;
import com.kenzz.crazyapp.skin.SkinResource;

/**
 * Created by huangdefa on 28/08/2017.
 * 换肤类型：
 *  背景，字体颜色，图片的src
 */

public enum  SkinType {
    TEXT_COLOR("textColor") {
        @Override
        public void skin(View view,String resName) {
            ColorStateList color = getSkinResource().getColorByName(resName);
            if(view instanceof TextView){
                TextView textView= (TextView) view;
                textView.setTextColor(color);
            }
        }
    },BACKGROUND("background") {
        @Override
        public void skin(View view,String resName) {
         //背景有可能是图片或是颜色
            Drawable drawable = getSkinResource().getDrawableByName(resName);
            if(drawable!=null){
                view.setBackgroundDrawable(drawable);
                return;
            }
            ColorStateList colorByName = getSkinResource().getColorByName(resName);
            if(colorByName!=null){
                view.setBackgroundColor(colorByName.getDefaultColor());
            }
        }
    },SRC("src") {
        @Override
        public void skin(View view,String resName) {
         if(view instanceof ImageView){
             ImageView imageView= (ImageView) view;
             Drawable drawable = getSkinResource().getDrawableByName(resName);
             imageView.setImageDrawable(drawable);
         }
        }
    };
    private String mResName;
    SkinType(String resName) {
        this.mResName =resName;
    }

    public abstract void skin(View view,String resName);

    public String getResName() {
        return mResName;
    }

    public SkinResource getSkinResource(){
        return SkinManager.getInstance().getSkinResource();
    }
}
