package com.test.okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.theokhttp.TheClientManager;
import com.github.theokhttp.TheOkHttp;

import java.io.IOException;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initData();
    }

    private void initData() {
        String url="https://wanandroid.com/wxarticle/list/408/1/json?k=Java";
        TheOkHttp.startGet(url, new MyCallback<String>() {
            @Override
            public void success(String response) {
                Log.i("=====","====="+response);
            }
            @Override
            public void error(Exception e) {
                e.printStackTrace();
                if(e instanceof IOException){
                    if("Canceled".equalsIgnoreCase(e.getMessage())){
                        Log.i("=====","==22222222222==="+e.getMessage());
                    }
                }
                Log.i("=====","====="+e.getMessage());
            }
            @Override
            public void cancel(Exception e) {
                super.cancel(e);
                Log.i("=====","==cancel===");
            }
            @Override
            public void noNetwork(Exception e) {
                super.noNetwork(e);
                Log.i("=====","==noNetwork===");
            }
        });
        TheClientManager.get().cancelRequest("a1");
        finish();

    }
}
