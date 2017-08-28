package com.kenzz.crazyapp.skin.attr;

import android.view.View;

/**
 * Created by huangdefa on 28/08/2017.
 * 换肤类型：
 *  背景，字体颜色，图片的src
 */

public enum  SkinType {
    TEXT_COLOR("text_color") {
        @Override
        public void skin(View view,String resName) {

        }
    },BACKGROUND("background") {
        @Override
        public void skin(View view,String resName) {

        }
    },SRC("src") {
        @Override
        public void skin(View view,String resName) {

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
}
