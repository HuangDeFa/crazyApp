package com.kenzz.crazyapp;

import org.junit.Test;

import java.util.ArrayList;
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

    @Test
    public void okHttpTest(){
        Retrofit retrofit=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private interface TestRetrofitService{
        @GET()
        Observable<?> getRepo();
    }
}



