package com.kenzz.crazyapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.kenzz.crazyapp.activitys.SkinBaseActivity;
import com.kenzz.crazyapp.annotations.OnClickEvent;
import com.kenzz.crazyapp.annotations.ViewById;
import com.kenzz.crazyapp.service.GuardService;
import com.kenzz.crazyapp.service.JobWakeUpService;
import com.kenzz.crazyapp.service.WorkService;
import com.kenzz.crazyapp.skin.SkinManager;
import com.kenzz.crazyapp.utils.ViewUtils;
import com.kenzz.crazyapp.widget.HeightLightTextView;
import com.kenzz.crazyapp.widget.QQRunView;

import java.io.File;

public class MainActivity extends SkinBaseActivity {

    @ViewById(R.id.btn)
    private Button mButton;
    @ViewById(R.id.btn2)
    private Button mButton2;
    @ViewById(R.id.myQQRun)
    private QQRunView mQQRunView;
    @ViewById(R.id.heightLightText)
    private HeightLightTextView mHeightLightTextView;
    private static final int REQ_PERMISSION=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        checkPermisssion();

        mQQRunView.setMaxCount(4000);
        ValueAnimator animator = ObjectAnimator.ofInt(0, 2700);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mQQRunView.setRunCount((Integer) animation.getAnimatedValue());
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(2000);
        animator.start();
        startService(new Intent(this, WorkService.class));
        startService(new Intent(MainActivity.this, GuardService.class));
        startService(new Intent(this, JobWakeUpService.class));

        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mHeightLightTextView.setProgress((Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationRepeat(Animator animation) {
                if(mHeightLightTextView.getDirection()==HeightLightTextView.LTR){
                    mHeightLightTextView.setDirection(HeightLightTextView.RTL);
                }else {
                    mHeightLightTextView.setDirection(HeightLightTextView.LTR);
                }
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        valueAnimator.setRepeatMode(ObjectAnimator.RESTART);
        valueAnimator.start();
    }

    @OnClickEvent({R.id.btn, R.id.btn2})
    public void onButtonClick(View view) {
        if (view.getId() == R.id.btn2) {
            Toast.makeText(this, "Click btn2", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btn) {
            Toast.makeText(this, "换肤", Toast.LENGTH_SHORT).show();
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath() + File.separator + "test.skin";
            SkinManager.getInstance().loadSkin(path);
        }
    }

    private void checkPermisssion(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQ_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQ_PERMISSION && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            //TODO load Skin or do other thing
        }else {
            finish();
        }
    }
}
