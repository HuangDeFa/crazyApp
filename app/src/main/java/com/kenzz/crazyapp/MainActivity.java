package com.kenzz.crazyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kenzz.crazyapp.annotations.OnClickEvent;
import com.kenzz.crazyapp.annotations.ViewById;
import com.kenzz.crazyapp.utils.ViewUtils;

public class MainActivity extends AppCompatActivity {

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
         Toast.makeText(this,"Click btn",Toast.LENGTH_SHORT).show();
     }
    }
}
