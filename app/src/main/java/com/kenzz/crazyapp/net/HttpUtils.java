package com.kenzz.crazyapp.net;

import android.content.Context;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangdefa on 27/08/2017.
 * Http 工具类
 */

public class HttpUtils {
    private static IHttpEngine mIHttpEngine;
    private Context mContext;
    private Map<String, Object> mParams;
    //请求类型
    public static final String GET = "get";
    public static final String POST = "post";

    @StringDef({
            GET, POST
    })
    public @interface RequestTypeModel {
    }

    private String mType;
    private String mBaseUrl;
    private HttpCallBack mHttpCallBack;
    private String mUrl;

    private HttpUtils(Context context) {
        mContext = context;
        mParams = new HashMap<>();
        mType = GET;
    }

    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    //方便再Application里面初始化默认http引擎
    public static void init(Context context) {
        mIHttpEngine = new OkHttpEngine(context);
    }

    //切换http引擎
    public HttpUtils exchangeHttpEngine(IHttpEngine httpEngine) {
        mIHttpEngine = httpEngine;
        return this;
    }

    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @return
     */
    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public HttpUtils addParams(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    public HttpUtils setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        return this;
    }

    public HttpUtils setHttpCallBack(HttpCallBack httpCallBack) {
        mHttpCallBack = httpCallBack;
        return this;
    }

    /**
     * @param type 请求类型
     *             {@link #GET},{@link #POST}
     *             默认是 {@link #GET}
     * @return
     */
    public HttpUtils setRequestType(@RequestTypeModel String type) {
        if (type != GET && type != POST) {
            throw new IllegalArgumentException("Http request type must be get or post");
        }
        mType = type;
        return this;
    }

    public void execute() {
        if (TextUtils.isEmpty(mUrl)) {
            if (TextUtils.isEmpty(mBaseUrl)) {
                throw new IllegalArgumentException("Base url can not be empty");
            }
            mUrl = mBaseUrl;
        }
        if (mHttpCallBack == null) {
            mHttpCallBack = HttpCallBack.DEFAULTCALLBACK;
        }
        mHttpCallBack.onPreExecute(mParams);
        combineParams(mParams);
        if (mType == GET) {
            doGet();
        } else if (mType == POST) {
            doPost();
        }
    }

    private void combineParams(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        //TODO 组装参数
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                //是字符串则直接拼接就可，若是文件等就必须另外处理
                if (entry.getValue() instanceof String) {
                    sb.append(entry.getKey())
                            .append("=")
                            .append(entry.getValue())
                            .append("&");
                }
            }
        }
        String queryString = sb.substring(sb.length() - 1);
        if (mBaseUrl.endsWith("/"))
            mUrl = mBaseUrl + "?" + queryString;
        else
            mUrl = mBaseUrl + File.separator+ "?" + queryString;
    }

    /**
     * 传入参数进行请求
     *
     * @param baseUrl      {@link #mBaseUrl 设置请求基地址 例如:https://github.com/}
     * @param params       {@link #mParams 设置请求参数}
     * @param httpCallBack {@link #mHttpCallBack 设置回调}
     */
    public void execute(String baseUrl, Map<String, Object> params, HttpCallBack httpCallBack) {
        mBaseUrl = baseUrl;
        mParams = params;
        mHttpCallBack = httpCallBack;
        execute();
    }

    public void execute(String url, HttpCallBack httpCallBack) {
        mUrl = url;
        mHttpCallBack = httpCallBack;
        execute();
    }

    private void doGet() {
        mIHttpEngine.get(mUrl, mParams, mHttpCallBack);
    }

    private void doPost() {
    }


}
