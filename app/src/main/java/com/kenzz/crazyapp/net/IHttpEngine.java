package com.kenzz.crazyapp.net;

import java.util.Map;

/**
 * Created by huangdefa on 27/08/2017.
 *  HTTP 工作引擎接口
 */

public interface IHttpEngine {
    void get(String url, Map<String,Object> params,HttpCallBack httpCallBack);
}
