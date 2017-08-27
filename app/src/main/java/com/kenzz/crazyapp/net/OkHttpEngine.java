package com.kenzz.crazyapp.net;

import android.content.Context;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huangdefa on 27/08/2017.
 * 使用OkHttp3实现
 */

public class OkHttpEngine implements IHttpEngine {

    private Context mContext;
    private OkHttpClient mHttpClient;

    public OkHttpEngine(Context context) {
        mContext = context;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置httpClient
        mHttpClient = builder.build();
    }

    @Override
    public void get(String url, Map<String, Object> params, final HttpCallBack httpCallBack) {
        Request.Builder builder=new Request.Builder();
        builder.url(url).get();
        mHttpClient.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpCallBack.onFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    httpCallBack.onSuccess(response.body().string());
                }
            }
        });
    }
}
