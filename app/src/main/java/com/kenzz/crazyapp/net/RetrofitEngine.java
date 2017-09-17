package com.kenzz.crazyapp.net;

import android.content.Context;

import java.io.IOException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;

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

        @Headers({"Accept:application/vnd.github.com","User-Agent:balabala","ContentType:text/plain"})
        @GET()
        Observable<Call<?>> getObservable(String url, Map<String,Object> params, HttpCallBack httpCallBack);
    }

    public RetrofitEngine(Context context) {
       mContext=context;
        mBuilder=new Retrofit.Builder();
        mBuilder.addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    //添加拦截器，给所以请求添加请求头信息
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request.Builder request=new Request.Builder();
                        request.addHeader("user_id","12444");
                        request.addHeader("ContentType","text/plain");
                        return chain.proceed(request.build());
                    }
                }).build());
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

    public void getObservable(String url, Map<String, Object> params, final HttpCallBack httpCallBack){
        mRetrofit.create(HttpEngineApi.class).getObservable(url,params,httpCallBack)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Call<?>, ObservableSource<Response<?>>>() {
                    @Override
                    public ObservableSource<Response<?>> apply(@NonNull Call<?> call) throws Exception {
                        Response<?> execute = call.execute();
                        return null;
                    }
                })
                .subscribe(new Observer<Response<?>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<?> response) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
