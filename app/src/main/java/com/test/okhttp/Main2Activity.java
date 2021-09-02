package com.test.okhttp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.github.theokhttp.TheOkHttp;
import com.google.gson.Gson;
import com.test.okhttp.bean.OtherDataRes;
import com.test.okhttp.bean.TabDataRes;

import java.util.HashMap;
import java.util.List;
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
        String url = "https://wanandroid.com/wxarticle/chapters/json";

        TheOkHttp.get().start(url, new MyCallback<List<TabDataRes>>() {
            @Override
            public void success(List<TabDataRes> response) {
                tvContent.setText(new Gson().toJson(response));
                tvPrompt.setText("请求成功");
            }

            @Override
            public void error(Exception e) {
                tvPrompt.setText("请求失败");
                tvContent.setText(e.getMessage());
            }
        });
    }
    private void paramGet() {
        String url = "https://wanandroid.com/wxarticle/list/408/1/json";
        Map<String, String> map = new HashMap<String, String>();
        map.put("k", "Java");

        TheOkHttp.get(map).start(url, new MyCallback<OtherDataRes>() {
            @Override
            public void success(OtherDataRes response) {
                tvContent.setText(response.toString());
                tvPrompt.setText("请求成功");
            }

            @Override
            public void error(Exception e) {
                tvPrompt.setText("请求失败");
                tvContent.setText(e.getMessage());
            }
        });
    }

    private void noParamPost() {
        String url = "https://wanandroid.com/wxarticle/chapters/json";
        TheOkHttp.post().start(url, new MyCallback<String>() {
            @Override
            public void success(String response) {
                tvContent.setText(response);
                tvPrompt.setText("请求成功");
            }

            @Override
            public void error(Exception e) {
                tvPrompt.setText("请求失败");
                tvContent.setText(e.getMessage());
            }
        });
    }

    private void paramPost() {
        String url = "https://wanandroid.com/wxarticle/list/408/1/json";
        Map<String, String> map = new HashMap<String, String>();
        map.put("k", "Java");
        //该接口不支持post请求，不管了，这里反正能发送post，至于返回结果无所谓了
        TheOkHttp.post(map).start(url, new MyCallback<String>() {
            @Override
            public void success(String response) {
                tvContent.setText(response);
                tvPrompt.setText("请求成功");
            }
            @Override
            public void error(Exception e) {
                tvPrompt.setText("请求失败");
                tvContent.setText(e.getMessage());
            }
        });
    }
}
