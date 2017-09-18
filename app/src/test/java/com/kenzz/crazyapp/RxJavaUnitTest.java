package com.kenzz.crazyapp;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by huangdefa on 16/09/2017.
 * Version 1.0
 * 用于学习RxJava 单元测试类避免启动apk的运行。
 */

public class RxJavaUnitTest {

    private static String TAG = RxJavaUnitTest.class.getSimpleName();

    @Test
    public void rxJavaCreateTest() {

        //创建方式 最多可以放是个对象参数
        Observable<Integer> just = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
        //可变参数形式，也可以是个数组
        Observable<String> fromArray = Observable.fromArray("a", "b", "c");
        //正规套路 需要传递一个ObservableOnSubscribe对象，其中在subscrie的回调方法进行事件流的发射，这就
        //需要ObservableEmitter 发射器进行实现. 发射器可以发出多个onNext实际，但只能发送一个onComplete/onError
        //事件，这两个事件是互斥的
       Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                try {
                    if(!emitter.isDisposed()) {
                        String[] ss = new String[]{"rxJava", "rxAndroid", "IOS", "Android"};
                        for (String s : ss) {
                            emitter.onNext(s);
                        }
                        emitter.onComplete();
                    }
                }catch (Exception e){
                    emitter.onError(e);
                }
            }
        });
        //观察者
        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("onSubscribe");
                // Log.d(TAG,"onSubscribe");
            }

            @Override
            public void onNext(@NonNull Object o) {
                System.out.println(o);
                // Log.d(TAG,o.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
                // Log.d(TAG,"onComplete");
            }
        };

        //range,interval
        Observable<Long> timer = Observable.timer(1000, TimeUnit.MILLISECONDS).map(new Function<Long, Long>() {
            @Override
            public Long apply(@NonNull Long aLong) throws Exception {
                return 20000000l;
            }
        });

        timer.subscribe(observer);
        just.subscribe(observer);
        fromArray.subscribe(observer);
        observable.subscribe(observer);
        Observable.defer(new Callable<ObservableSource<?>>() {
            @Override
            public ObservableSource<?> call() throws Exception {
                return null;
            }
        });

        //Subject AsyncSubject->只发送onComplete事件的前一个，后面的不发送
        AsyncSubject<String> asyncSubject = AsyncSubject.create();
        asyncSubject.onNext("async1");
        asyncSubject.onNext("async2");
        asyncSubject.onNext("async3");
        asyncSubject.onComplete();
        asyncSubject.onNext("async24");
        asyncSubject.subscribeWith(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("asyncSub1-->onSubscribe");
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println("asyncSub1-->" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("asyncSub1-->onComplete");
            }
        });
        asyncSubject.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("asyncSub2-->" + s);
            }
        });

        //PublishSubject 必须在发送事件之前订阅，而且只发送在订阅之后onComplete事件之前的事件流
        PublishSubject<String> publishSubject = PublishSubject.create();

        publishSubject.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("pubSub-->onSubscribe");
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println("pubSub-->" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("pubSub-->onComplete");
            }
        });
        publishSubject.onNext("pub1");
        publishSubject.onNext("pub2");
        publishSubject.onNext("pub3");
        publishSubject.onComplete();

        // ReplaySubject 会发送所有事件，不管在何时订阅
        ReplaySubject<String> replaySubject = ReplaySubject.create();
        replaySubject.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("reply-->onSubscribe");
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println("reply-->" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("reply-->onComplete");
            }
        });
        replaySubject.onNext("reply1");
        replaySubject.onNext("reply2");
        replaySubject.onNext("reply3");
        replaySubject.onComplete();

        //BehaviorSubject 发送订阅前的一个事件和订阅后的事件
        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create();
        behaviorSubject.onNext("behavior-->1");
        behaviorSubject.onNext("behavior-->2");
        behaviorSubject.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("behavior-->onSubscribe");
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println("behavior-->" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("behavior-->onComplete");
            }
        });
        behaviorSubject.onNext("behavior-->3");
        behaviorSubject.onComplete();

        //Map 转换操作，将一系列可观测数据系列转换为.Map操作默认发生不依赖任何调度器
        Observable.fromArray("ob1", "bo2", "bo3")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        return "map_" + s;
                    }
                })
                //表示重复发送次数
                .repeat(2)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("Map-->" + s);
                    }
                });

        //flatMap Map操作之后返回Observables(observable("ob1"),observable("ob2"),observable("ob3"));
        //然后汇总到一个Observable中，但是不保证后面observable发送事件的顺序
        Observable.fromArray("ob1", "bo2", "bo3")
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull String s) throws Exception {
                        return Observable.just(s);
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String o) throws Exception {
                System.out.println("flatMap-->"+o);
            }
        });
        //concatMap Map操作之后返回Observables(observable("ob1"),observable("ob2"),observable("ob3"));
        //然后汇总到一个Observable中，将所有observable平铺按顺序发射数据
        Observable.fromArray("ob1", "bo2", "bo3")
                .concatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull String s) throws Exception {
                        return Observable.just(s);
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String o) throws Exception {
             System.out.println("contactMap-->"+o);
            }
        });

       //take(count) 表示提取前面count个数据,takeLast(count) 表示提取后面count个数据
        Observable.fromArray(1,2,3,4,5)
                .take(3)
                .takeLast(2)
                .observeOn(Schedulers.computation())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                       System.out.println("take-->"+integer);
                    }
                });
        //range(start,count) 表示发射从start开始的count个数据 ,
        // intervalRange(0,10,500,1000, TimeUnit.SECONDS)-->表示发送从0开始的10个数据
        //延迟500s执行，周期为1000s,单位事s. 相当于一个Timer触发器
        Observable.range(0,10)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer>5;
                    }
                })
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("range-->"+integer);
            }
        });
        //interval()函数的两个参数：一个指定两次发射的时间间隔，另一个是用到的时间单位。
       /* Observable.interval(200,500,TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {

            }
        });
        */

       //concat操作符将多个observable连接起来，只要前面一个ob发射完数据，
        // 接着将观察者订阅到第二个接个第二个发射数据如此类推
        // Observable.concat(a,b)==a.concatWith(b)
        Observable ob1=Observable.just(1,2,3);
        Observable ob2=Observable.just("a","b","c");
        Observable.concat(ob1,ob2)
                .subscribe(new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("concat-->onSubscribe");
            }

            @Override
            public void onNext(@NonNull Object o) {
                System.out.println("concat-->"+o);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("concat-->onComplete");
            }
        });

        Observable.range(1,20).any(new Predicate<Integer>() {
            @Override
            public boolean test(@NonNull Integer integer) throws Exception {
                return integer>10;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                System.out.println("any-->" +aBoolean);
            }
        });

        //调度器 不仅能用在Observable上面指定Observable发送数据操作发生的线程或者指定调用Observer方法发生的线程
        //同时也可以单独出来使用创建worker进行操作： worker.dispose() 停止worker队列里面任务释放资源
        final Scheduler.Worker worker = Schedulers.newThread().createWorker();
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<50;i++){
                    System.out.println("scheduler_worker-->"+i);
                }
               // worker.dispose();
            }
        });
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("scheduler_worker2-->");
            }
        });

        Observable.fromIterable(new ArrayList<String>(){{add("Android");add("IOS");add("WindowPhone");}})
                .forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                       System.out.println(s);
                    }
                });

        //组合 将前一个数据流的最后一个和下一个数据流进行combine
        Observable.combineLatest(Observable.just(2, 6), Observable.just(5, 6, 7, 8), Observable.just(1, 9), new Function3<Integer, Integer, Integer, String>() {
            @Override
            public String apply(@NonNull Integer integer, @NonNull Integer integer2, @NonNull Integer integer3) throws Exception {
                return String.format("integer %1s,integer2 %2s,integer3 %3s",integer,integer2,integer3);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String integer) throws Exception {
                System.out.println("combineLatest-->"+integer);
            }
        });
    }

    /**
     * OkHttp 是一个Http client客户端
     * 主要类：OkHttpClient,OkHttpClient.Builder -->创建client用于包装发起请求
     * 主要用于设置：拦截器，添加代理，添加证书，连接超时等
     * Request,Request.Builder-->创建一个请求
     *
     * 辅助：RequestBody-->FormBody 创建一个表单(键值对)请求体，MultipartBody分块上传可以组合多个请求体
     *
     * 主要包装了：url,请求的方法，请求头的信息，携带RequestBody信息(一般是Post请求）例如文件的上传
     * Response,Response.Builder-->创建一个响应
     * 辅助：ResponseBody-->CacheResponse 缓存响应，设置了缓存策略。NetResponse 网络响应，服务器返回的响应
     * 主要包装了响应信息：message,状态码code,还有一系列ResponseBody
     * ResponseBody 真正的响应内容 包括contentLength,InputStream,byte[]和Reader
     * Interceptor 拦截器：主要作用于整个请求响应过程，拦截这个过程做一下预处理或者添加一下共同的操作。
     * 例如为每个请求添加同样的请求头，或者拦截响应打印一些log信息等~
     *
     * call，是发起一个请求的包装器。通过call可以执行同步、异步的HTTP请求。Http请求会导致线程阻塞，所以
     * 真正的操作要放到子线程去，call.execute()同步返回Response。call.enqueue(callBack)异步通过callBack
     * 进行响应回调。如果涉及到UI需要用Handler切换到主线程。
     *
     * Cache:缓存，okHttp内置了缓存策略和实现，只要手动打开设置就可以
     *
     *
     * http://lf.snssdk.com/neihan/service/tabs/?essence=1&iid=3216590132&device_id=32613520945&ac=wifi&channel=360&aid=7&
     * app_name=joke_essay&version_code=612&version_name=6.1.2&device_platform=android&ssmix=a&
     * device_type=sansung&device_brand=xiaomi&os_api=28&os_version=6.10.1&uuid=326135942187625&
     * openudid=3dg6s95rhg2a3dg5&manifest_version_code=612&resolution=1450*2800&dpi=620&update_version_code=6120
     */
    @Test
    public void okHttpTest(){
        String url="http://lf.snssdk.com/neihan/service/tabs/?essence=1&iid=3216590132&device_id=32613520945&ac=wifi&channel=360&aid=7" +
                "&app_name=joke_essay&version_code=612&version_name=6.1.2&device_platform=android" +
                "&ssmix=a&device_type=sansung&device_brand=xiaomi&os_api=28&os_version=6.10.1" +
                "&uuid=326135942187625&openudid=3dg6s95rhg2a3dg5&manifest_version_code=612&" +
                "resolution=1450*2800&dpi=620&update_version_code=6120";
        OkHttpClient.Builder clientBuilder=new OkHttpClient.Builder();
        Request request=new Request.Builder()
                .url(url)
                .get()
                .build();
        try {

            clientBuilder.readTimeout(5,TimeUnit.SECONDS);
            clientBuilder.connectTimeout(5,TimeUnit.SECONDS);
            //设置缓存
            //clientBuilder.cache(null);
            clientBuilder.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                   Response response= chain.proceed(chain.request());
                    Response.Builder builder = response.newBuilder();
                    builder.addHeader("joke_essay","hello world");
                    return builder.build();
                }
            });

          Response response = clientBuilder.build().newCall(request).execute();
            if(response.code()==200){
                Headers headers = response.headers();
                if(headers!=null){
                    Map<String, List<String>> stringListMap = headers.toMultimap();
                    if(stringListMap!=null && stringListMap.size()>0) {
                        Observable.fromIterable(stringListMap.entrySet()).flatMap(new Function<Map.Entry<String, List<String>>, ObservableSource<String>>() {
                            @Override
                            public ObservableSource<String> apply(@NonNull final Map.Entry<String, List<String>> stringListEntry) throws Exception {
                                return Observable.fromIterable(stringListEntry.getValue()).map(new Function<String, String>() {
                                    @Override
                                    public String apply(@NonNull String s) throws Exception {
                                        return stringListEntry.getKey()+": "+s;
                                    }
                                });
                            }
                        }).subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                System.out.println(s);
                            }
                        });
                    }
                }
                InputStream inputStream = response.body().byteStream();
                if(inputStream!=null){
                    ByteArrayOutputStream bos=new ByteArrayOutputStream();
                    byte[] temp=new byte[1024*1024];
                    int len;
                    while ((len=inputStream.read(temp))!=-1){
                        bos.write(temp,0,len);
                    }
                    inputStream.close();
                    System.out.println(bos.toString("utf-8"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* 采用内置的HttpUrlConnection 进行网络请求的套路
        HttpURLConnection urlConnection=null;
        try {
            URL con=new URL(url);
            urlConnection= (HttpURLConnection) con.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode==200){
                InputStream inputStream = urlConnection.getInputStream();
                BufferedInputStream bis=new BufferedInputStream(inputStream);
                byte[] temp=new byte[1024*1024];
                int len = bis.read(temp, 0, temp.length);
                while (len!=-1){
                   len = bis.read(temp, 0, temp.length);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
        }
        */
    }

    /**
     *
     */
    @Test
    public void retrofitTest(){
        Retrofit retrofit=new Retrofit.Builder()

                .build();
    }

    private Cache provideCache(String path,long size){
        return new Cache(new File(path),size);
    }

    /**
     *  上传
     * @param url
     * @param params
     */
    private void doPost(String url,Map<String,String> params){
        RequestBody requestBody= new FormBody.Builder()
                .add("userName","WongNima")
                .add("gender","sez")
                .build();

        RequestBody body=new MultipartBody.Builder()
                .addPart(Headers.of("Content-Disposition","form-data;name=\"title\""),RequestBody.create(null,"hello world"))
                .addPart(Headers.of("Content-Disposition","form-data;name=\"title\""),RequestBody.create(MediaType.parse("image/png"),new File("aaa.png")))
                .build();
    }

    private interface TestRetrofitService{
        @GET()
        Observable<?> getRepo();
    }
}



