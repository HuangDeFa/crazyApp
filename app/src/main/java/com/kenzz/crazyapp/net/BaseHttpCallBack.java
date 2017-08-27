package com.kenzz.crazyapp.net;

import java.util.Map;

/**
 * Created by huangdefa on 27/08/2017.
 *  BaseHttpCallBack 实现preExecute方法 进行请求参数的拼接
 *  因为往往在同一个业务中请求的参数很多都是公用的。
 */

public abstract class BaseHttpCallBack implements HttpCallBack {
    @Override
    public void onPreExecute(Map<String,Object> params) {
     //TODO 可以写一些和业务相关的逻辑 这里可以处理一下公用参数的配置
    }
}
