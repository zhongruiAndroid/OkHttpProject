package com.test.okhttp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.theokhttp.TheOkHttp;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    TextView tvPrompt;
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tvPrompt = findViewById(R.id.tvPrompt);
        tvContent = findViewById(R.id.tvContent);

        Intent intent = getIntent();
        String action = intent.getAction();
        switch (action) {
            case "0":
                //无参get
                noParamGet();
                break;
            case "1":
                //有参get
                paramGet();
                break;
            case "2":
                //无参post
                noParamPost();
                break;
            case "3":
                //有参post
                paramPost();
                break;
        }
    }

    private void noParamGet() {
        String url="https://wanandroid.com/wxarticle/chapters/json";

        TheOkHttp.get().start(url, new MyCallback<String>() {
            @Override
            public void success(String response) {
                tvContent.setText(response);
                tvPrompt.setText("请求成功");
            }
            @Override
            public void error(Exception e) {
                tvPrompt.setText("请求失败");
            }
        });
    }
    private void paramGet() {
        String url="https://wanandroid.com/wxarticle/list/408/1/json";
        Map<String,String> map=new HashMap<String,String>();
        map.put("k","Java");

        TheOkHttp.get(map).start(url, new MyCallback<String>() {
            @Override
            public void success(String response) {
                tvContent.setText(response);
                tvPrompt.setText("请求成功");
            }
            @Override
            public void error(Exception e) {
                tvPrompt.setText("请求失败");
            }
        });

    }
    private void noParamPost() {
        String url="https://wanandroid.com/wxarticle/chapters/json";
        TheOkHttp.post().start(url, new MyCallback<String>() {
            @Override
            public void success(String response) {
                tvContent.setText(response);
                tvPrompt.setText("请求成功");
            }
            @Override
            public void error(Exception e) {
                tvPrompt.setText("请求失败");
            }
        });
    }
    private void paramPost() {
        String url="https://wanandroid.com/wxarticle/list/408/1/json";
        Map<String,String> map=new HashMap<String,String>();
        map.put("k","Java");

        TheOkHttp.get(map).start(url, new MyCallback<String>() {
            @Override
            public void success(String response) {
                tvContent.setText(response);
                tvPrompt.setText("请求成功");
            }
            @Override
            public void error(Exception e) {
                tvPrompt.setText("请求失败");
            }
        });
    }
}
