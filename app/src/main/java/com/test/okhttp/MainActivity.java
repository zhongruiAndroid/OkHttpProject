package com.test.okhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.theokhttp.TheOk;
import com.github.theokhttp.TheOkHttpCallback;
import com.google.gson.Gson;
import com.test.okhttp.bean.BaseBean;
import com.test.okhttp.bean.TabDataRes;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.btPost).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btPost:
                post1();
            break;
        }
    }

    private void post1() {
        String url="https://wanandroid.com/wxarticle/chapters/json";
        OkHttpTool.request(url, new MyCallback<BaseBean<List<TabDataRes>>>() {
            @Override
            public void success(BaseBean<List<TabDataRes>> data) {
                Log.e("=====",getStr(data));
            }
            @Override
            public void error(Exception e) {
                loge(e.getMessage());
            }
        });
    }
    private void loge(String string){
        Log.e("=====","==:"+string);
    }
    private String getStr(Object object){
        return new Gson().toJson(object);
    }
}
