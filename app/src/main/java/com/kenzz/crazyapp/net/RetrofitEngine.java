package com.kenzz.crazyapp.net;

import android.content.Context;

import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by huangdefa on 27/08/2017.
 * Retrofit 实现的HttpEngine
 */

public class RetrofitEngine implements IHttpEngine {

    private Retrofit mRetrofit;
    private Context mContext;
    private Retrofit.Builder mBuilder;

    public interface HttpEngineApi{
        @GET()
        Call get(String url, Map<String,Object> params, HttpCallBack httpCallBack);
    }

    public RetrofitEngine(Context context) {
       mContext=context;
        mBuilder=new Retrofit.Builder();
        mBuilder.addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient());
        mRetrofit=mBuilder.build();
    }

    @Override
    public void get(String url, Map<String, Object> params, final HttpCallBack httpCallBack) {
        mRetrofit.create(HttpEngineApi.class).get(url,params,httpCallBack).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                httpCallBack.onSuccess(response.body().toString());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
             httpCallBack.onFailed(new Exception(t));
            }
        });
    }
}
