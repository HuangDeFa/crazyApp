package com.kenzz.crazyapp;

import android.os.Environment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
    }

    @OnClickEvent({R.id.btn,R.id.btn2})
    public void onButtonClick(View view){
     if(view.getId()==R.id.btn2){
         Toast.makeText(this,"Click btn2",Toast.LENGTH_SHORT).show();
     }else if(view.getId()==R.id.btn){
         Toast.makeText(this,"换肤",Toast.LENGTH_SHORT).show();
        String path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath()+ File.separator+"app-debug.apk";
         SkinManager.getInstance().loadSkin(path);
     }
    }
}
