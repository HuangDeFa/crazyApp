package com.kenzz.crazyapp.service;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.List;

/**
 * Created by huangdefa on 10/09/2017.
 * Version 1.0
 * 使用
 */

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {
    final static int jobServiceId=1;
    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        //获取 JobScheduler,进行轮询
        JobScheduler jobScheduler= (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo=new JobInfo.Builder(jobServiceId,new ComponentName(this,JobWakeUpService.class.getName()))
                .setPeriodic(2000)
                .build();
        jobScheduler.schedule(jobInfo);
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        if(!isServiceAlive(WorkService.class.getName())){
           startService(new Intent(this,WorkService.class));
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private boolean isServiceAlive(String serviceName){
      ActivityManager manager= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo serviceInfon:runningServices){
          if(serviceInfon.service.getClassName().equals(serviceName)){
             return  true;
          }
        }
        return false;
    }

}
