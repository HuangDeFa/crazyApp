package com.kenzz.crazyapp.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kenzz.crazyapp.IWorkAIDL;

/**
 * Created by huangdefa on 09/09/2017.
 *  守护进程
 */

public class GuardService extends Service {
    static String TAG=GuardService.class.getSimpleName();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStart-->");
        bindService(new Intent(this,WorkService.class),mGuardConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy-->");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind-->");
        return super.onUnbind(intent);
    }

    private ServiceConnection mGuardConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onServiceDisconnected-->");
            startService(new Intent(GuardService.this,WorkService.class));
            bindService(new Intent(GuardService.this,WorkService.class),mGuardConnection, Context.BIND_IMPORTANT);
        }
    };

    private IWorkAIDL.Stub mBinder=new IWorkAIDL.Stub() {
        @Override
        public void doWork() throws RemoteException {

        }
    };
}
