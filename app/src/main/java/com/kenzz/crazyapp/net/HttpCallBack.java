package com.kenzz.crazyapp.net;

import java.util.Map;
import java.util.Objects;

/**
 * Created by huangdefa on 27/08/2017.
 * Http 请求回调接口
 */

public interface HttpCallBack {

    void onPreExecute(Map<String,Object> params);

    void onFailed(Exception e);

    void onSuccess(String result);

    //默认空回调
    HttpCallBack DEFAULTCALLBACK=new HttpCallBack() {
        @Override
        public void onPreExecute(Map<String,Object> params) {

        }

        @Override
        public void onFailed(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
