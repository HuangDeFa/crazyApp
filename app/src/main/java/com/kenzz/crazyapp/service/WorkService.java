package com.kenzz.crazyapp.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kenzz.crazyapp.IWorkAIDL;

/**
 * Created by huangdefa on 09/09/2017.
 * 工作线程
 */

public class WorkService extends Service {
    static String TAG=WorkService.class.getSimpleName();
    final static int WorkServiceId=1;
    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Log.d(TAG, "--do work-->");
                        Thread.sleep(300);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind--->");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind--->");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand--->");
        //提高服务的优先级
        //startForeground(WorkServiceId,new Notification());
        bindService(new Intent(this,GuardService.class),mWorkConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy--->");
        super.onDestroy();
    }

    private ServiceConnection  mWorkConnection =new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            startService(new Intent(WorkService.this,GuardService.class));
            bindService(new Intent(WorkService.this,GuardService.class),mWorkConnection, Context.BIND_IMPORTANT);
        }
    };

    private IWorkAIDL.Stub mBinder=new IWorkAIDL.Stub() {
        @Override
        public void doWork() throws RemoteException {

        }
    };

}
