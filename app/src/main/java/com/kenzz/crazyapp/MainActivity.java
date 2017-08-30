package com.kenzz.crazyapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kenzz.crazyapp.activitys.SkinBaseActivity;
import com.kenzz.crazyapp.annotations.OnClickEvent;
import com.kenzz.crazyapp.annotations.ViewById;
import com.kenzz.crazyapp.skin.SkinManager;
import com.kenzz.crazyapp.utils.ViewUtils;

import java.io.File;

public class MainActivity extends SkinBaseActivity {

    @ViewById(R.id.btn)
    private Button mButton;
    @ViewById(R.id.btn2)
    private Button mButton2;
    private static final int REQ_PERMISSION=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        checkPermisssion();
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
